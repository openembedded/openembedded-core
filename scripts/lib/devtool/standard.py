# Development tool - standard commands plugin
#
# Copyright (C) 2014-2015 Intel Corporation
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

import os
import sys
import re
import shutil
import glob
import tempfile
import logging
import argparse
from devtool import exec_build_env_command, setup_tinfoil

logger = logging.getLogger('devtool')

def plugin_init(pluginlist):
    pass


def add(args, config, basepath, workspace):
    import bb
    import oe.recipeutils

    if args.recipename in workspace:
        logger.error("recipe %s is already in your workspace" % args.recipename)
        return -1

    reason = oe.recipeutils.validate_pn(args.recipename)
    if reason:
        logger.error(reason)
        return -1

    srctree = os.path.abspath(args.srctree)
    appendpath = os.path.join(config.workspace_path, 'appends')
    if not os.path.exists(appendpath):
        os.makedirs(appendpath)

    recipedir = os.path.join(config.workspace_path, 'recipes', args.recipename)
    bb.utils.mkdirhier(recipedir)
    if args.version:
        if '_' in args.version or ' ' in args.version:
            logger.error('Invalid version string "%s"' % args.version)
            return -1
        bp = "%s_%s" % (args.recipename, args.version)
    else:
        bp = args.recipename
    recipefile = os.path.join(recipedir, "%s.bb" % bp)
    if sys.stdout.isatty():
        color = 'always'
    else:
        color = args.color
    stdout, stderr = exec_build_env_command(config.init_path, basepath, 'recipetool --color=%s create -o %s %s' % (color, recipefile, srctree))
    logger.info('Recipe %s has been automatically created; further editing may be required to make it fully functional' % recipefile)

    _add_md5(config, args.recipename, recipefile)

    initial_rev = None
    if os.path.exists(os.path.join(srctree, '.git')):
        (stdout, _) = bb.process.run('git rev-parse HEAD', cwd=srctree)
        initial_rev = stdout.rstrip()

    appendfile = os.path.join(appendpath, '%s.bbappend' % args.recipename)
    with open(appendfile, 'w') as f:
        f.write('inherit externalsrc\n')
        f.write('EXTERNALSRC = "%s"\n' % srctree)
        if args.same_dir:
            f.write('EXTERNALSRC_BUILD = "%s"\n' % srctree)
        if initial_rev:
            f.write('\n# initial_rev: %s\n' % initial_rev)

    _add_md5(config, args.recipename, appendfile)

    return 0


def _check_compatible_recipe(pn, d):
    if pn == 'perf':
        logger.error("The perf recipe does not actually check out source and thus cannot be supported by this tool")
        return False

    if pn in ['gcc-source', 'kernel-devsrc', 'package-index']:
        logger.error("The %s recipe is not supported by this tool" % pn)
        return False

    if bb.data.inherits_class('image', d):
        logger.error("The %s recipe is an image, and therefore is not supported by this tool" % pn)
        return False

    if bb.data.inherits_class('populate_sdk', d):
        logger.error("The %s recipe is an SDK, and therefore is not supported by this tool" % pn)
        return False

    if bb.data.inherits_class('packagegroup', d):
        logger.error("The %s recipe is a packagegroup, and therefore is not supported by this tool" % pn)
        return False

    if bb.data.inherits_class('meta', d):
        logger.error("The %s recipe is a meta-recipe, and therefore is not supported by this tool" % pn)
        return False

    if bb.data.inherits_class('externalsrc', d) and d.getVar('EXTERNALSRC', True):
        logger.error("externalsrc is currently enabled for the %s recipe. This prevents the normal do_patch task from working. You will need to disable this first." % pn)
        return False

    return True


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


def extract(args, config, basepath, workspace):
    import bb
    import oe.recipeutils

    tinfoil = setup_tinfoil()

    recipefile = _get_recipe_file(tinfoil.cooker, args.recipename)
    if not recipefile:
        # Error already logged
        return -1
    rd = oe.recipeutils.parse_recipe(recipefile, tinfoil.config_data)

    srctree = os.path.abspath(args.srctree)
    initial_rev = _extract_source(srctree, args.keep_temp, args.branch, rd)
    if initial_rev:
        return 0
    else:
        return -1


def _extract_source(srctree, keep_temp, devbranch, d):
    import bb.event

    def eventfilter(name, handler, event, d):
        if name == 'base_eventhandler':
            return True
        else:
            return False

    if hasattr(bb.event, 'set_eventfilter'):
        bb.event.set_eventfilter(eventfilter)

    pn = d.getVar('PN', True)

    if not _check_compatible_recipe(pn, d):
        return None

    if os.path.exists(srctree):
        if not os.path.isdir(srctree):
            logger.error("output path %s exists and is not a directory" % srctree)
            return None
        elif os.listdir(srctree):
            logger.error("output path %s already exists and is non-empty" % srctree)
            return None

    # Prepare for shutil.move later on
    bb.utils.mkdirhier(srctree)
    os.rmdir(srctree)

    initial_rev = None
    tempdir = tempfile.mkdtemp(prefix='devtool')
    try:
        crd = d.createCopy()
        # Make a subdir so we guard against WORKDIR==S
        workdir = os.path.join(tempdir, 'workdir')
        crd.setVar('WORKDIR', workdir)
        crd.setVar('T', os.path.join(tempdir, 'temp'))
        if not crd.getVar('S', True).startswith(workdir):
            # Usually a shared workdir recipe (kernel, gcc)
            # Try to set a reasonable default
            if bb.data.inherits_class('kernel', d):
                crd.setVar('S', '${WORKDIR}/source')
            else:
                crd.setVar('S', '${WORKDIR}/${BP}')
        if bb.data.inherits_class('kernel', d):
            # We don't want to move the source to STAGING_KERNEL_DIR here
            crd.setVar('STAGING_KERNEL_DIR', '${S}')

        # FIXME: This is very awkward. Unfortunately it's not currently easy to properly
        # execute tasks outside of bitbake itself, until then this has to suffice if we
        # are to handle e.g. linux-yocto's extra tasks
        executed = []
        def exec_task_func(func, report):
            if not func in executed:
                deps = crd.getVarFlag(func, 'deps')
                if deps:
                    for taskdepfunc in deps:
                        exec_task_func(taskdepfunc, True)
                if report:
                    logger.info('Executing %s...' % func)
                fn = d.getVar('FILE', True)
                localdata = bb.build._task_data(fn, func, crd)
                bb.build.exec_func(func, localdata)
                executed.append(func)

        logger.info('Fetching %s...' % pn)
        exec_task_func('do_fetch', False)
        logger.info('Unpacking...')
        exec_task_func('do_unpack', False)
        srcsubdir = crd.getVar('S', True)
        if srcsubdir != workdir and os.path.dirname(srcsubdir) != workdir:
            # Handle if S is set to a subdirectory of the source
            srcsubdir = os.path.join(workdir, os.path.relpath(srcsubdir, workdir).split(os.sep)[0])

        if os.path.exists(os.path.join(srcsubdir, '.git')):
            alternatesfile = os.path.join(srcsubdir, '.git', 'objects', 'info', 'alternates')
            if os.path.exists(alternatesfile):
                # This will have been cloned with -s, so we need to convert it to a full clone
                bb.process.run('git repack -a', cwd=srcsubdir)
                os.remove(alternatesfile)

        patchdir = os.path.join(srcsubdir, 'patches')
        haspatches = False
        if os.path.exists(patchdir):
            if os.listdir(patchdir):
                haspatches = True
            else:
                os.rmdir(patchdir)

        if bb.data.inherits_class('kernel-yocto', d):
            (stdout, _) = bb.process.run('git --git-dir="%s" rev-parse HEAD' % crd.expand('${WORKDIR}/git'), cwd=srcsubdir)
            initial_rev = stdout.rstrip()
        else:
            if not os.listdir(srcsubdir):
                logger.error("no source unpacked to S, perhaps the %s recipe doesn't use any source?" % pn)
                return None

            if not os.path.exists(os.path.join(srcsubdir, '.git')):
                bb.process.run('git init', cwd=srcsubdir)
                bb.process.run('git add .', cwd=srcsubdir)
                bb.process.run('git commit -q -m "Initial commit from upstream at version %s"' % crd.getVar('PV', True), cwd=srcsubdir)

            (stdout, _) = bb.process.run('git rev-parse HEAD', cwd=srcsubdir)
            initial_rev = stdout.rstrip()

            bb.process.run('git checkout -b %s' % devbranch, cwd=srcsubdir)
            bb.process.run('git tag -f devtool-base', cwd=srcsubdir)

            crd.setVar('PATCHTOOL', 'git')

        logger.info('Patching...')
        exec_task_func('do_patch', False)

        bb.process.run('git tag -f devtool-patched', cwd=srcsubdir)

        if os.path.exists(patchdir):
            shutil.rmtree(patchdir)
            if haspatches:
                bb.process.run('git checkout patches', cwd=srcsubdir)

        shutil.move(srcsubdir, srctree)
        logger.info('Source tree extracted to %s' % srctree)
    finally:
        if keep_temp:
            logger.info('Preserving temporary directory %s' % tempdir)
        else:
            shutil.rmtree(tempdir)
    return initial_rev

def _add_md5(config, recipename, filename):
    import bb.utils
    md5 = bb.utils.md5_file(filename)
    with open(os.path.join(config.workspace_path, '.devtool_md5'), 'a') as f:
        f.write('%s|%s|%s\n' % (recipename, os.path.relpath(filename, config.workspace_path), md5))

def _check_preserve(config, recipename):
    import bb.utils
    origfile = os.path.join(config.workspace_path, '.devtool_md5')
    newfile = os.path.join(config.workspace_path, '.devtool_md5_new')
    preservepath = os.path.join(config.workspace_path, 'attic')
    with open(origfile, 'r') as f:
        with open(newfile, 'w') as tf:
            for line in f.readlines():
                splitline = line.rstrip().split('|')
                if splitline[0] == recipename:
                    removefile = os.path.join(config.workspace_path, splitline[1])
                    md5 = bb.utils.md5_file(removefile)
                    if splitline[2] != md5:
                        bb.utils.mkdirhier(preservepath)
                        preservefile = os.path.basename(removefile)
                        logger.warn('File %s modified since it was written, preserving in %s' % (preservefile, preservepath))
                        shutil.move(removefile, os.path.join(preservepath, preservefile))
                    else:
                        os.remove(removefile)
                else:
                    tf.write(line)
    os.rename(newfile, origfile)

    return False


def modify(args, config, basepath, workspace):
    import bb
    import oe.recipeutils

    if args.recipename in workspace:
        logger.error("recipe %s is already in your workspace" % args.recipename)
        return -1

    if not args.extract:
        if not os.path.isdir(args.srctree):
            logger.error("directory %s does not exist or not a directory (specify -x to extract source from recipe)" % args.srctree)
            return -1

    tinfoil = setup_tinfoil()

    recipefile = _get_recipe_file(tinfoil.cooker, args.recipename)
    if not recipefile:
        # Error already logged
        return -1
    rd = oe.recipeutils.parse_recipe(recipefile, tinfoil.config_data)

    if not _check_compatible_recipe(args.recipename, rd):
        return -1

    initial_rev = None
    commits = []
    srctree = os.path.abspath(args.srctree)
    if args.extract:
        initial_rev = _extract_source(args.srctree, False, args.branch, rd)
        if not initial_rev:
            return -1
        # Get list of commits since this revision
        (stdout, _) = bb.process.run('git rev-list --reverse %s..HEAD' % initial_rev, cwd=args.srctree)
        commits = stdout.split()
    else:
        if os.path.exists(os.path.join(args.srctree, '.git')):
            # Check if it's a tree previously extracted by us
            try:
                (stdout, _) = bb.process.run('git branch --contains devtool-base', cwd=args.srctree)
            except bb.process.ExecutionError:
                stdout = ''
            for line in stdout.splitlines():
                if line.startswith('*'):
                    (stdout, _) = bb.process.run('git rev-parse devtool-base', cwd=args.srctree)
                    initial_rev = stdout.rstrip()
            if not initial_rev:
                # Otherwise, just grab the head revision
                (stdout, _) = bb.process.run('git rev-parse HEAD', cwd=args.srctree)
                initial_rev = stdout.rstrip()

    # Check that recipe isn't using a shared workdir
    s = rd.getVar('S', True)
    workdir = rd.getVar('WORKDIR', True)
    if s.startswith(workdir):
        # Handle if S is set to a subdirectory of the source
        if s != workdir and os.path.dirname(s) != workdir:
            srcsubdir = os.sep.join(os.path.relpath(s, workdir).split(os.sep)[1:])
            srctree = os.path.join(srctree, srcsubdir)

    appendpath = os.path.join(config.workspace_path, 'appends')
    if not os.path.exists(appendpath):
        os.makedirs(appendpath)

    appendname = os.path.splitext(os.path.basename(recipefile))[0]
    if args.wildcard:
        appendname = re.sub(r'_.*', '_%', appendname)
    appendfile = os.path.join(appendpath, appendname + '.bbappend')
    with open(appendfile, 'w') as f:
        f.write('FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"\n\n')
        f.write('inherit externalsrc\n')
        f.write('# NOTE: We use pn- overrides here to avoid affecting multiple variants in the case where the recipe uses BBCLASSEXTEND\n')
        f.write('EXTERNALSRC_pn-%s = "%s"\n' % (args.recipename, srctree))
        if args.same_dir or bb.data.inherits_class('autotools-brokensep', rd):
            if args.same_dir:
                logger.info('using source tree as build directory since --same-dir specified')
            else:
                logger.info('using source tree as build directory since original recipe inherits autotools-brokensep')
            f.write('EXTERNALSRC_BUILD_pn-%s = "%s"\n' % (args.recipename, srctree))
        if initial_rev:
            f.write('\n# initial_rev: %s\n' % initial_rev)
            for commit in commits:
                f.write('# commit: %s\n' % commit)

    _add_md5(config, args.recipename, appendfile)

    logger.info('Recipe %s now set up to build from %s' % (args.recipename, srctree))

    return 0


def update_recipe(args, config, basepath, workspace):
    if not args.recipename in workspace:
        logger.error("no recipe named %s in your workspace" % args.recipename)
        return -1

    # Get initial revision from bbappend
    appends = glob.glob(os.path.join(config.workspace_path, 'appends', '%s_*.bbappend' % args.recipename))
    if not appends:
        logger.error('unable to find workspace bbappend for recipe %s' % args.recipename)
        return -1

    tinfoil = setup_tinfoil()
    import bb
    from oe.patch import GitApplyTree
    import oe.recipeutils

    recipefile = _get_recipe_file(tinfoil.cooker, args.recipename)
    if not recipefile:
        # Error already logged
        return -1
    rd = oe.recipeutils.parse_recipe(recipefile, tinfoil.config_data)

    orig_src_uri = rd.getVar('SRC_URI', False) or ''
    if args.mode == 'auto':
        if 'git://' in orig_src_uri:
            mode = 'srcrev'
        else:
            mode = 'patch'
    else:
        mode = args.mode

    def remove_patches(srcuri, patchlist):
        # Remove any patches that we don't need
        updated = False
        for patch in patchlist:
            patchfile = os.path.basename(patch)
            for i in xrange(len(srcuri)):
                if srcuri[i].startswith('file://') and os.path.basename(srcuri[i]).split(';')[0] == patchfile:
                    logger.info('Removing patch %s' % patchfile)
                    srcuri.pop(i)
                    # FIXME "git rm" here would be nice if the file in question is tracked
                    # FIXME there's a chance that this file is referred to by another recipe, in which case deleting wouldn't be the right thing to do
                    if patch.startswith(os.path.dirname(recipefile)):
                        os.remove(patch)
                    updated = True
                    break
        return updated

    srctree = workspace[args.recipename]
    if mode == 'srcrev':
        (stdout, _) = bb.process.run('git rev-parse HEAD', cwd=srctree)
        srcrev = stdout.strip()
        if len(srcrev) != 40:
            logger.error('Invalid hash returned by git: %s' % stdout)
            return 1

        logger.info('Updating SRCREV in recipe %s' % os.path.basename(recipefile))
        patchfields = {}
        patchfields['SRCREV'] = srcrev
        if not args.no_remove:
            # Find list of existing patches in recipe file
            existing_patches = oe.recipeutils.get_recipe_patches(rd)

            old_srcrev = (rd.getVar('SRCREV', False) or '')
            tempdir = tempfile.mkdtemp(prefix='devtool')
            removepatches = []
            try:
                GitApplyTree.extractPatches(srctree, old_srcrev, tempdir)
                newpatches = os.listdir(tempdir)
                for patch in existing_patches:
                    patchfile = os.path.basename(patch)
                    if patchfile in newpatches:
                        removepatches.append(patch)
            finally:
                shutil.rmtree(tempdir)
            if removepatches:
                srcuri = (rd.getVar('SRC_URI', False) or '').split()
                if remove_patches(srcuri, removepatches):
                    patchfields['SRC_URI'] = ' '.join(srcuri)

        oe.recipeutils.patch_recipe(rd, recipefile, patchfields)

        if not 'git://' in orig_src_uri:
            logger.info('You will need to update SRC_URI within the recipe to point to a git repository where you have pushed your changes')

    elif mode == 'patch':
        commits = []
        update_rev = None
        if args.initial_rev:
            initial_rev = args.initial_rev
        else:
            initial_rev = None
            with open(appends[0], 'r') as f:
                for line in f:
                    if line.startswith('# initial_rev:'):
                        initial_rev = line.split(':')[-1].strip()
                    elif line.startswith('# commit:'):
                        commits.append(line.split(':')[-1].strip())

            if initial_rev:
                # Find first actually changed revision
                (stdout, _) = bb.process.run('git rev-list --reverse %s..HEAD' % initial_rev, cwd=srctree)
                newcommits = stdout.split()
                for i in xrange(min(len(commits), len(newcommits))):
                    if newcommits[i] == commits[i]:
                        update_rev = commits[i]

        if not initial_rev:
            logger.error('Unable to find initial revision - please specify it with --initial-rev')
            return -1

        if not update_rev:
            update_rev = initial_rev

        # Find list of existing patches in recipe file
        existing_patches = oe.recipeutils.get_recipe_patches(rd)

        removepatches = []
        if not args.no_remove:
            # Get all patches from source tree and check if any should be removed
            tempdir = tempfile.mkdtemp(prefix='devtool')
            try:
                GitApplyTree.extractPatches(srctree, initial_rev, tempdir)
                newpatches = os.listdir(tempdir)
                for patch in existing_patches:
                    patchfile = os.path.basename(patch)
                    if patchfile not in newpatches:
                        removepatches.append(patch)
            finally:
                shutil.rmtree(tempdir)

        # Get updated patches from source tree
        tempdir = tempfile.mkdtemp(prefix='devtool')
        try:
            GitApplyTree.extractPatches(srctree, update_rev, tempdir)

            # Match up and replace existing patches with corresponding new patches
            updatepatches = False
            updaterecipe = False
            newpatches = os.listdir(tempdir)
            for patch in existing_patches:
                patchfile = os.path.basename(patch)
                if patchfile in newpatches:
                    logger.info('Updating patch %s' % patchfile)
                    shutil.move(os.path.join(tempdir, patchfile), patch)
                    newpatches.remove(patchfile)
                    updatepatches = True
            srcuri = (rd.getVar('SRC_URI', False) or '').split()
            if newpatches:
                # Add any patches left over
                patchdir = os.path.join(os.path.dirname(recipefile), rd.getVar('BPN', True))
                bb.utils.mkdirhier(patchdir)
                for patchfile in newpatches:
                    logger.info('Adding new patch %s' % patchfile)
                    shutil.move(os.path.join(tempdir, patchfile), os.path.join(patchdir, patchfile))
                    srcuri.append('file://%s' % patchfile)
                    updaterecipe = True
            if removepatches:
                if remove_patches(srcuri, removepatches):
                    updaterecipe = True
            if updaterecipe:
                logger.info('Updating recipe %s' % os.path.basename(recipefile))
                oe.recipeutils.patch_recipe(rd, recipefile, {'SRC_URI': ' '.join(srcuri)})
            elif not updatepatches:
                # Neither patches nor recipe were updated
                logger.info('No patches need updating')
        finally:
            shutil.rmtree(tempdir)

    else:
        logger.error('update_recipe: invalid mode %s' % mode)
        return 1

    return 0


def status(args, config, basepath, workspace):
    if workspace:
        for recipe, value in workspace.iteritems():
            print("%s: %s" % (recipe, value))
    else:
        logger.info('No recipes currently in your workspace - you can use "devtool modify" to work on an existing recipe or "devtool add" to add a new one')
    return 0


def reset(args, config, basepath, workspace):
    import bb.utils
    if args.recipename:
        if args.all:
            logger.error("Recipe cannot be specified if -a/--all is used")
            return -1
        elif not args.recipename in workspace:
            logger.error("no recipe named %s in your workspace" % args.recipename)
            return -1
    elif not args.all:
        logger.error("Recipe must be specified, or specify -a/--all to reset all recipes")
        return -1

    if args.all:
        recipes = workspace
    else:
        recipes = [args.recipename]

    for pn in recipes:
        if not args.no_clean:
            logger.info('Cleaning sysroot for recipe %s...' % pn)
            exec_build_env_command(config.init_path, basepath, 'bitbake -c clean %s' % pn)

        _check_preserve(config, pn)

        preservepath = os.path.join(config.workspace_path, 'attic', pn)
        def preservedir(origdir):
            if os.path.exists(origdir):
                for fn in os.listdir(origdir):
                    logger.warn('Preserving %s in %s' % (fn, preservepath))
                    bb.utils.mkdirhier(preservepath)
                    shutil.move(os.path.join(origdir, fn), os.path.join(preservepath, fn))
                os.rmdir(origdir)

        preservedir(os.path.join(config.workspace_path, 'recipes', pn))
        # We don't automatically create this dir next to appends, but the user can
        preservedir(os.path.join(config.workspace_path, 'appends', pn))

    return 0


def build(args, config, basepath, workspace):
    import bb
    if not args.recipename in workspace:
        logger.error("no recipe named %s in your workspace" % args.recipename)
        return -1
    build_task = config.get('Build', 'build_task', 'populate_sysroot')
    exec_build_env_command(config.init_path, basepath, 'bitbake -c %s %s' % (build_task, args.recipename), watch=True)

    return 0


def register_commands(subparsers, context):
    parser_add = subparsers.add_parser('add', help='Add a new recipe',
                                       description='Adds a new recipe',
                                       formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_add.add_argument('recipename', help='Name for new recipe to add')
    parser_add.add_argument('srctree', help='Path to external source tree')
    parser_add.add_argument('--same-dir', '-s', help='Build in same directory as source', action="store_true")
    parser_add.add_argument('--version', '-V', help='Version to use within recipe (PV)')
    parser_add.set_defaults(func=add)

    parser_modify = subparsers.add_parser('modify', help='Modify the source for an existing recipe',
                                       description='Enables modifying the source for an existing recipe',
                                       formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_modify.add_argument('recipename', help='Name for recipe to edit')
    parser_modify.add_argument('srctree', help='Path to external source tree')
    parser_modify.add_argument('--wildcard', '-w', action="store_true", help='Use wildcard for unversioned bbappend')
    parser_modify.add_argument('--extract', '-x', action="store_true", help='Extract source as well')
    parser_modify.add_argument('--same-dir', '-s', help='Build in same directory as source', action="store_true")
    parser_modify.add_argument('--branch', '-b', default="devtool", help='Name for development branch to checkout (only when using -x)')
    parser_modify.set_defaults(func=modify)

    parser_extract = subparsers.add_parser('extract', help='Extract the source for an existing recipe',
                                       description='Extracts the source for an existing recipe',
                                       formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_extract.add_argument('recipename', help='Name for recipe to extract the source for')
    parser_extract.add_argument('srctree', help='Path to where to extract the source tree')
    parser_extract.add_argument('--branch', '-b', default="devtool", help='Name for development branch to checkout')
    parser_extract.add_argument('--keep-temp', action="store_true", help='Keep temporary directory (for debugging)')
    parser_extract.set_defaults(func=extract)

    parser_update_recipe = subparsers.add_parser('update-recipe', help='Apply changes from external source tree to recipe',
                                       description='Applies changes from external source tree to a recipe (updating/adding/removing patches as necessary, or by updating SRCREV)')
    parser_update_recipe.add_argument('recipename', help='Name of recipe to update')
    parser_update_recipe.add_argument('--mode', '-m', choices=['patch', 'srcrev', 'auto'], default='auto', help='Update mode (where %(metavar)s is %(choices)s; default is %(default)s)', metavar='MODE')
    parser_update_recipe.add_argument('--initial-rev', help='Starting revision for patches')
    parser_update_recipe.add_argument('--no-remove', '-n', action="store_true", help='Don\'t remove patches, only add or update')
    parser_update_recipe.set_defaults(func=update_recipe)

    parser_status = subparsers.add_parser('status', help='Show workspace status',
                                          description='Lists recipes currently in your workspace and the paths to their respective external source trees',
                                          formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_status.set_defaults(func=status)

    parser_build = subparsers.add_parser('build', help='Build a recipe',
                                         description='Builds the specified recipe using bitbake',
                                         formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_build.add_argument('recipename', help='Recipe to build')
    parser_build.set_defaults(func=build)

    parser_reset = subparsers.add_parser('reset', help='Remove a recipe from your workspace',
                                         description='Removes the specified recipe from your workspace (resetting its state)',
                                         formatter_class=argparse.ArgumentDefaultsHelpFormatter)
    parser_reset.add_argument('recipename', nargs='?', help='Recipe to reset')
    parser_reset.add_argument('--all', '-a', action="store_true", help='Reset all recipes (clear workspace)')
    parser_reset.add_argument('--no-clean', '-n', action="store_true", help='Don\'t clean the sysroot to remove recipe output')
    parser_reset.set_defaults(func=reset)
