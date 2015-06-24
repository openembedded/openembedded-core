# Recipe creation tool - append plugin
#
# Copyright (C) 2015 Intel Corporation
#
# This program is free software; you can redistribute it and/or modify
# it under the terms of the GNU General Public License version 2 as
# published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License along
# with this program; if not, write to the Free Software Foundation, Inc.,
# 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.

import sys
import os
import argparse
import glob
import fnmatch
import re
import subprocess
import logging
import stat
import shutil
import scriptutils
import errno
from collections import defaultdict

logger = logging.getLogger('recipetool')

tinfoil = None

def plugin_init(pluginlist):
    # Don't need to do anything here right now, but plugins must have this function defined
    pass

def tinfoil_init(instance):
    global tinfoil
    tinfoil = instance


# FIXME guessing when we don't have pkgdata?
# FIXME mode to create patch rather than directly substitute

class InvalidTargetFileError(Exception):
    pass

def find_target_file(targetpath, d, pkglist=None):
    """Find the recipe installing the specified target path, optionally limited to a select list of packages"""
    import json

    pkgdata_dir = d.getVar('PKGDATA_DIR', True)

    # The mix between /etc and ${sysconfdir} here may look odd, but it is just
    # being consistent with usage elsewhere
    invalidtargets = {'${sysconfdir}/version': '${sysconfdir}/version is written out at image creation time',
                      '/etc/timestamp': '/etc/timestamp is written out at image creation time',
                      '/dev/*': '/dev is handled by udev (or equivalent) and the kernel (devtmpfs)',
                      '/etc/passwd': '/etc/passwd should be managed through the useradd and extrausers classes',
                      '/etc/group': '/etc/group should be managed through the useradd and extrausers classes',
                      '/etc/shadow': '/etc/shadow should be managed through the useradd and extrausers classes',
                      '/etc/gshadow': '/etc/gshadow should be managed through the useradd and extrausers classes',
                      '${sysconfdir}/hostname': '${sysconfdir}/hostname contents should be set by setting hostname_pn-base-files = "value" in configuration',}

    for pthspec, message in invalidtargets.iteritems():
        if fnmatch.fnmatchcase(targetpath, d.expand(pthspec)):
            raise InvalidTargetFileError(d.expand(message))

    targetpath_re = re.compile(r'\s+(\$D)?%s(\s|$)' % targetpath)

    recipes = defaultdict(list)
    for root, dirs, files in os.walk(os.path.join(pkgdata_dir, 'runtime')):
        if pkglist:
            filelist = pkglist
        else:
            filelist = files
        for fn in filelist:
            pkgdatafile = os.path.join(root, fn)
            if pkglist and not os.path.exists(pkgdatafile):
                continue
            with open(pkgdatafile, 'r') as f:
                pn = ''
                # This does assume that PN comes before other values, but that's a fairly safe assumption
                for line in f:
                    if line.startswith('PN:'):
                        pn = line.split(':', 1)[1].strip()
                    elif line.startswith('FILES_INFO:'):
                        val = line.split(':', 1)[1].strip()
                        dictval = json.loads(val)
                        for fullpth in dictval.keys():
                            if fnmatch.fnmatchcase(fullpth, targetpath):
                                recipes[targetpath].append(pn)
                    elif line.startswith('pkg_preinst_') or line.startswith('pkg_postinst_'):
                        scriptval = line.split(':', 1)[1].strip().decode('string_escape')
                        if 'update-alternatives --install %s ' % targetpath in scriptval:
                            recipes[targetpath].append('?%s' % pn)
                        elif targetpath_re.search(scriptval):
                            recipes[targetpath].append('!%s' % pn)
    return recipes

def _get_recipe_file(cooker, pn):
    import oe.recipeutils
    recipefile = oe.recipeutils.pn_to_recipe(cooker, pn)
    if not recipefile:
        skipreasons = oe.recipeutils.get_unavailable_reasons(cooker, pn)
        if skipreasons:
            logger.error('\n'.join(skipreasons))
        else:
            logger.error("Unable to find any recipe file matching %s" % pn)
    return recipefile

def _parse_recipe(pn, tinfoil):
    import oe.recipeutils
    recipefile = _get_recipe_file(tinfoil.cooker, pn)
    if not recipefile:
        # Error already logged
        return None
    append_files = tinfoil.cooker.collection.get_file_appends(recipefile)
    rd = oe.recipeutils.parse_recipe(recipefile, append_files,
                                    tinfoil.config_data)
    return rd

def determine_file_source(targetpath, rd):
    """Assuming we know a file came from a specific recipe, figure out exactly where it came from"""
    import oe.recipeutils

    # See if it's in do_install for the recipe
    workdir = rd.getVar('WORKDIR', True)
    src_uri = rd.getVar('SRC_URI', True)
    srcfile = ''
    modpatches = []
    elements = check_do_install(rd, targetpath)
    if elements:
        logger.debug('do_install line:\n%s' % ' '.join(elements))
        srcpath = get_source_path(elements)
        logger.debug('source path: %s' % srcpath)
        if not srcpath.startswith('/'):
            # Handle non-absolute path
            srcpath = os.path.abspath(os.path.join(rd.getVarFlag('do_install', 'dirs', True).split()[-1], srcpath))
        if srcpath.startswith(workdir):
            # OK, now we have the source file name, look for it in SRC_URI
            workdirfile = os.path.relpath(srcpath, workdir)
            # FIXME this is where we ought to have some code in the fetcher, because this is naive
            for item in src_uri.split():
                localpath = bb.fetch2.localpath(item, rd)
                # Source path specified in do_install might be a glob
                if fnmatch.fnmatch(os.path.basename(localpath), workdirfile):
                    srcfile = 'file://%s' % localpath
                elif '/' in workdirfile:
                    if item == 'file://%s' % workdirfile:
                        srcfile = 'file://%s' % localpath

        # Check patches
        srcpatches = []
        patchedfiles = oe.recipeutils.get_recipe_patched_files(rd)
        for patch, filelist in patchedfiles.iteritems():
            for fileitem in filelist:
                if fileitem[0] == srcpath:
                    srcpatches.append((patch, fileitem[1]))
        if srcpatches:
            addpatch = None
            for patch in srcpatches:
                if patch[1] == 'A':
                    addpatch = patch[0]
                else:
                    modpatches.append(patch[0])
            if addpatch:
                srcfile = 'patch://%s' % addpatch

    return (srcfile, elements, modpatches)

def get_source_path(cmdelements):
    """Find the source path specified within a command"""
    command = cmdelements[0]
    if command in ['install', 'cp']:
        helptext = subprocess.check_output('LC_ALL=C %s --help' % command, shell=True)
        argopts = ''
        argopt_line_re = re.compile('^-([a-zA-Z0-9]), --[a-z-]+=')
        for line in helptext.splitlines():
            line = line.lstrip()
            res = argopt_line_re.search(line)
            if res:
                argopts += res.group(1)
        if not argopts:
            # Fallback
            if command == 'install':
                argopts = 'gmoSt'
            elif command == 'cp':
                argopts = 't'
            else:
                raise Exception('No fallback arguments for command %s' % command)

        skipnext = False
        for elem in cmdelements[1:-1]:
            if elem.startswith('-'):
                if len(elem) > 1 and elem[1] in argopts:
                    skipnext = True
                continue
            if skipnext:
                skipnext = False
                continue
            return elem
    else:
        raise Exception('get_source_path: no handling for command "%s"')

def get_func_deps(func, d):
    """Find the function dependencies of a shell function"""
    deps = bb.codeparser.ShellParser(func, logger).parse_shell(d.getVar(func, True))
    deps |= set((d.getVarFlag(func, "vardeps", True) or "").split())
    funcdeps = []
    for dep in deps:
        if d.getVarFlag(dep, 'func', True):
            funcdeps.append(dep)
    return funcdeps

def check_do_install(rd, targetpath):
    """Look at do_install for a command that installs/copies the specified target path"""
    instpath = os.path.abspath(os.path.join(rd.getVar('D', True), targetpath.lstrip('/')))
    do_install = rd.getVar('do_install', True)
    # Handle where do_install calls other functions (somewhat crudely, but good enough for this purpose)
    deps = get_func_deps('do_install', rd)
    for dep in deps:
        do_install = do_install.replace(dep, rd.getVar(dep, True))

    # Look backwards through do_install as we want to catch where a later line (perhaps
    # from a bbappend) is writing over the top
    for line in reversed(do_install.splitlines()):
        line = line.strip()
        if (line.startswith('install ') and ' -m' in line) or line.startswith('cp '):
            elements = line.split()
            destpath = os.path.abspath(elements[-1])
            if destpath == instpath:
                return elements
            elif destpath.rstrip('/') == os.path.dirname(instpath):
                # FIXME this doesn't take recursive copy into account; unsure if it's practical to do so
                srcpath = get_source_path(elements)
                if fnmatch.fnmatchcase(os.path.basename(instpath), os.path.basename(srcpath)):
                    return elements
    return None


def appendfile(args):
    import oe.recipeutils

    stdout = ''
    try:
        (stdout, _) = bb.process.run('LANG=C file -b %s' % args.newfile, shell=True)
        if 'cannot open' in stdout:
            raise bb.process.ExecutionError(stdout)
    except bb.process.ExecutionError as err:
        logger.debug('file command returned error: %s' % err)
        stdout = ''
    if stdout:
        logger.debug('file command output: %s' % stdout.rstrip())
        if ('executable' in stdout and not 'shell script' in stdout) or 'shared object' in stdout:
            logger.warn('This file looks like it is a binary or otherwise the output of compilation. If it is, you should consider building it properly instead of substituting a binary file directly.')

    if args.recipe:
        recipes = {args.targetpath: [args.recipe],}
    else:
        try:
            recipes = find_target_file(args.targetpath, tinfoil.config_data)
        except InvalidTargetFileError as e:
            logger.error('%s cannot be handled by this tool: %s' % (args.targetpath, e))
            return 1
        if not recipes:
            logger.error('Unable to find any package producing path %s - this may be because the recipe packaging it has not been built yet' % args.targetpath)
            return 1

    alternative_pns = []
    postinst_pns = []

    selectpn = None
    for targetpath, pnlist in recipes.iteritems():
        for pn in pnlist:
            if pn.startswith('?'):
                alternative_pns.append(pn[1:])
            elif pn.startswith('!'):
                postinst_pns.append(pn[1:])
            else:
                selectpn = pn

    if not selectpn and len(alternative_pns) == 1:
        selectpn = alternative_pns[0]
        logger.error('File %s is an alternative possibly provided by recipe %s but seemingly no other, selecting it by default - you should double check other recipes' % (args.targetpath, selectpn))

    if selectpn:
        logger.debug('Selecting recipe %s for file %s' % (selectpn, args.targetpath))
        if postinst_pns:
            logger.warn('%s be modified by postinstall scripts for the following recipes:\n  %s\nThis may or may not be an issue depending on what modifications these postinstall scripts make.' % (args.targetpath, '\n  '.join(postinst_pns)))
        rd = _parse_recipe(selectpn, tinfoil)
        if not rd:
            # Error message already shown
            return 1
        sourcefile, instelements, modpatches = determine_file_source(args.targetpath, rd)
        sourcepath = None
        if sourcefile:
            sourcetype, sourcepath = sourcefile.split('://', 1)
            logger.debug('Original source file is %s (%s)' % (sourcepath, sourcetype))
            if sourcetype == 'patch':
                logger.warn('File %s is added by the patch %s - you may need to remove or replace this patch in order to replace the file.' % (args.targetpath, sourcepath))
                sourcepath = None
        else:
            logger.debug('Unable to determine source file, proceeding anyway')
        if modpatches:
            logger.warn('File %s is modified by the following patches:\n  %s' % (args.targetpath, '\n  '.join(modpatches)))

        if instelements and sourcepath:
            install = None
        else:
            # Auto-determine permissions
            # Check destination
            binpaths = '${bindir}:${sbindir}:${base_bindir}:${base_sbindir}:${libexecdir}:${sysconfdir}/init.d'
            perms = '0644'
            if os.path.abspath(os.path.dirname(args.targetpath)) in rd.expand(binpaths).split(':'):
                # File is going into a directory normally reserved for executables, so it should be executable
                perms = '0755'
            else:
                # Check source
                st = os.stat(args.newfile)
                if st.st_mode & stat.S_IXUSR:
                    perms = '0755'
            install = {args.newfile: (args.targetpath, perms)}
        oe.recipeutils.bbappend_recipe(rd, args.destlayer, {args.newfile: sourcepath}, install, wildcardver=args.wildcard_version, machine=args.machine)
        return 0
    else:
        if alternative_pns:
            logger.error('File %s is an alternative possibly provided by the following recipes:\n  %s\nPlease select recipe with -r/--recipe' % (targetpath, '\n  '.join(alternative_pns)))
        elif postinst_pns:
            logger.error('File %s may be written out in a pre/postinstall script of the following recipes:\n  %s\nPlease select recipe with -r/--recipe' % (targetpath, '\n  '.join(postinst_pns)))
        return 3


def layer(layerpath):
    if not os.path.exists(os.path.join(layerpath, 'conf', 'layer.conf')):
        raise argparse.ArgumentTypeError('{0!r} must be a path to a valid layer'.format(layerpath))
    return layerpath


def existing_file(filepath):
    if not os.path.exists(filepath):
        raise argparse.ArgumentTypeError('{0!r} must be an existing path'.format(filepath))
    elif os.path.isdir(filepath):
        raise argparse.ArgumentTypeError('{0!r} must be a file, not a directory'.format(filepath))
    return filepath


def target_path(targetpath):
    if not os.path.isabs(targetpath):
        raise argparse.ArgumentTypeError('{0!r} must be an absolute path, not relative'.format(targetpath))
    return targetpath


def register_command(subparsers):
    parser_appendfile = subparsers.add_parser('appendfile',
                                              help='Create/update a bbappend to replace a file',
                                              description='Creates a bbappend (or updates an existing one) to replace the specified file that appears in the target system, determining the recipe that packages the file and the required path and name for the bbappend automatically. Note that the ability to determine the recipe packaging a particular file depends upon the recipe\'s do_packagedata task having already run prior to running this command (which it will have when the recipe has been built successfully, which in turn will have happened if one or more of the recipe\'s packages is included in an image that has been built successfully).')
    parser_appendfile.add_argument('destlayer', help='Base directory of the destination layer to write the bbappend to', type=layer)
    parser_appendfile.add_argument('targetpath', help='Path to the file to be replaced (as it would appear within the target image, e.g. /etc/motd)', type=target_path)
    parser_appendfile.add_argument('newfile', help='Custom file to replace the target file with', type=existing_file)
    parser_appendfile.add_argument('-r', '--recipe', help='Override recipe to apply to (default is to find which recipe already packages the file)')
    parser_appendfile.add_argument('-m', '--machine', help='Make bbappend changes specific to a machine only', metavar='MACHINE')
    parser_appendfile.add_argument('-w', '--wildcard-version', help='Use wildcard to make the bbappend apply to any recipe version', action='store_true')
    parser_appendfile.set_defaults(func=appendfile, parserecipes=True)
