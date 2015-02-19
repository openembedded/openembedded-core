# Utility functions for reading and modifying recipes
#
# Some code borrowed from the OE layer index
#
# Copyright (C) 2013-2014 Intel Corporation
#

import sys
import os
import os.path
import tempfile
import textwrap
import difflib
import utils
import shutil
import re
from collections import OrderedDict, defaultdict


# Help us to find places to insert values
recipe_progression = ['SUMMARY', 'DESCRIPTION', 'HOMEPAGE', 'BUGTRACKER', 'SECTION', 'LICENSE', 'LIC_FILES_CHKSUM', 'PROVIDES', 'DEPENDS', 'PR', 'PV', 'SRCREV', 'SRC_URI', 'S', 'do_fetch', 'do_unpack', 'do_patch', 'EXTRA_OECONF', 'do_configure', 'EXTRA_OEMAKE', 'do_compile', 'do_install', 'do_populate_sysroot', 'INITSCRIPT', 'USERADD', 'GROUPADD', 'PACKAGES', 'FILES', 'RDEPENDS', 'RRECOMMENDS', 'RSUGGESTS', 'RPROVIDES', 'RREPLACES', 'RCONFLICTS', 'ALLOW_EMPTY', 'do_package', 'do_deploy']
# Variables that sometimes are a bit long but shouldn't be wrapped
nowrap_vars = ['SUMMARY', 'HOMEPAGE', 'BUGTRACKER']
list_vars = ['SRC_URI', 'LIC_FILES_CHKSUM']
meta_vars = ['SUMMARY', 'DESCRIPTION', 'HOMEPAGE', 'BUGTRACKER', 'SECTION']


def pn_to_recipe(cooker, pn):
    """Convert a recipe name (PN) to the path to the recipe file"""
    import bb.providers

    if pn in cooker.recipecache.pkg_pn:
        filenames = cooker.recipecache.pkg_pn[pn]
        best = bb.providers.findBestProvider(pn, cooker.data, cooker.recipecache, cooker.recipecache.pkg_pn)
        return best[3]
    else:
        return None


def get_unavailable_reasons(cooker, pn):
    """If a recipe could not be found, find out why if possible"""
    import bb.taskdata
    taskdata = bb.taskdata.TaskData(None, skiplist=cooker.skiplist)
    return taskdata.get_reasons(pn)


def parse_recipe(fn, d):
    """Parse an individual recipe"""
    import bb.cache
    envdata = bb.cache.Cache.loadDataFull(fn, [], d)
    return envdata


def get_var_files(fn, varlist, d):
    """Find the file in which each of a list of variables is set.
    Note: requires variable history to be enabled when parsing.
    """
    envdata = parse_recipe(fn, d)
    varfiles = {}
    for v in varlist:
        history = envdata.varhistory.variable(v)
        files = []
        for event in history:
            if 'file' in event and not 'flag' in event:
                files.append(event['file'])
        if files:
            actualfile = files[-1]
        else:
            actualfile = None
        varfiles[v] = actualfile

    return varfiles


def patch_recipe_file(fn, values, patch=False, relpath=''):
    """Update or insert variable values into a recipe file (assuming you
       have already identified the exact file you want to update.)
       Note that some manual inspection/intervention may be required
       since this cannot handle all situations.
    """
    remainingnames = {}
    for k in values.keys():
        remainingnames[k] = recipe_progression.index(k) if k in recipe_progression else -1
    remainingnames = OrderedDict(sorted(remainingnames.iteritems(), key=lambda x: x[1]))

    with tempfile.NamedTemporaryFile('w', delete=False) as tf:
        def outputvalue(name):
            rawtext = '%s = "%s"\n' % (name, values[name])
            if name in nowrap_vars:
                tf.write(rawtext)
            elif name in list_vars:
                splitvalue = values[name].split()
                if len(splitvalue) > 1:
                    linesplit = ' \\\n' + (' ' * (len(name) + 4))
                    tf.write('%s = "%s%s"\n' % (name, linesplit.join(splitvalue), linesplit))
                else:
                    tf.write(rawtext)
            else:
                wrapped = textwrap.wrap(rawtext)
                for wrapline in wrapped[:-1]:
                    tf.write('%s \\\n' % wrapline)
                tf.write('%s\n' % wrapped[-1])

        tfn = tf.name
        with open(fn, 'r') as f:
            # First runthrough - find existing names (so we know not to insert based on recipe_progression)
            # Second runthrough - make the changes
            existingnames = []
            for runthrough in [1, 2]:
                currname = None
                for line in f:
                    if not currname:
                        insert = False
                        for k in remainingnames.keys():
                            for p in recipe_progression:
                                if re.match('^%s(_prepend|_append)*[ ?:=(]' % p, line):
                                    if remainingnames[k] > -1 and recipe_progression.index(p) > remainingnames[k] and runthrough > 1 and not k in existingnames:
                                        outputvalue(k)
                                        del remainingnames[k]
                                    break
                        for k in remainingnames.keys():
                            if re.match('^%s[ ?:=]' % k, line):
                                currname = k
                                if runthrough == 1:
                                    existingnames.append(k)
                                else:
                                    del remainingnames[k]
                                break
                        if currname and runthrough > 1:
                            outputvalue(currname)

                    if currname:
                        sline = line.rstrip()
                        if not sline.endswith('\\'):
                            currname = None
                        continue
                    if runthrough > 1:
                        tf.write(line)
                f.seek(0)
        if remainingnames:
            tf.write('\n')
            for k in remainingnames.keys():
                outputvalue(k)

    with open(tfn, 'U') as f:
        tolines = f.readlines()
    if patch:
        with open(fn, 'U') as f:
            fromlines = f.readlines()
        relfn = os.path.relpath(fn, relpath)
        diff = difflib.unified_diff(fromlines, tolines, 'a/%s' % relfn, 'b/%s' % relfn)
        os.remove(tfn)
        return diff
    else:
        with open(fn, 'w') as f:
            f.writelines(tolines)
        os.remove(tfn)
        return None

def localise_file_vars(fn, varfiles, varlist):
    """Given a list of variables and variable history (fetched with get_var_files())
    find where each variable should be set/changed. This handles for example where a
    recipe includes an inc file where variables might be changed - in most cases
    we want to update the inc file when changing the variable value rather than adding
    it to the recipe itself.
    """
    fndir = os.path.dirname(fn) + os.sep

    first_meta_file = None
    for v in meta_vars:
        f = varfiles.get(v, None)
        if f:
            actualdir = os.path.dirname(f) + os.sep
            if actualdir.startswith(fndir):
                first_meta_file = f
                break

    filevars = defaultdict(list)
    for v in varlist:
        f = varfiles[v]
        # Only return files that are in the same directory as the recipe or in some directory below there
        # (this excludes bbclass files and common inc files that wouldn't be appropriate to set the variable
        # in if we were going to set a value specific to this recipe)
        if f:
            actualfile = f
        else:
            # Variable isn't in a file, if it's one of the "meta" vars, use the first file with a meta var in it
            if first_meta_file:
                actualfile = first_meta_file
            else:
                actualfile = fn

        actualdir = os.path.dirname(actualfile) + os.sep
        if not actualdir.startswith(fndir):
            actualfile = fn
        filevars[actualfile].append(v)

    return filevars

def patch_recipe(d, fn, varvalues, patch=False, relpath=''):
    """Modify a list of variable values in the specified recipe. Handles inc files if
    used by the recipe.
    """
    varlist = varvalues.keys()
    varfiles = get_var_files(fn, varlist, d)
    locs = localise_file_vars(fn, varfiles, varlist)
    patches = []
    for f,v in locs.iteritems():
        vals = {k: varvalues[k] for k in v}
        patchdata = patch_recipe_file(f, vals, patch, relpath)
        if patch:
            patches.append(patchdata)

    if patch:
        return patches
    else:
        return None



def copy_recipe_files(d, tgt_dir, whole_dir=False, download=True):
    """Copy (local) recipe files, including both files included via include/require,
    and files referred to in the SRC_URI variable."""
    import bb.fetch2
    import oe.path

    # FIXME need a warning if the unexpanded SRC_URI value contains variable references

    uris = (d.getVar('SRC_URI', True) or "").split()
    fetch = bb.fetch2.Fetch(uris, d)
    if download:
        fetch.download()

    # Copy local files to target directory and gather any remote files
    bb_dir = os.path.dirname(d.getVar('FILE', True)) + os.sep
    remotes = []
    includes = [path for path in d.getVar('BBINCLUDED', True).split() if
                path.startswith(bb_dir) and os.path.exists(path)]
    for path in fetch.localpaths() + includes:
        # Only import files that are under the meta directory
        if path.startswith(bb_dir):
            if not whole_dir:
                relpath = os.path.relpath(path, bb_dir)
                subdir = os.path.join(tgt_dir, os.path.dirname(relpath))
                if not os.path.exists(subdir):
                    os.makedirs(subdir)
                shutil.copy2(path, os.path.join(tgt_dir, relpath))
        else:
            remotes.append(path)
    # Simply copy whole meta dir, if requested
    if whole_dir:
        shutil.copytree(bb_dir, tgt_dir)

    return remotes


def get_recipe_patches(d):
    """Get a list of the patches included in SRC_URI within a recipe."""
    patchfiles = []
    # Execute src_patches() defined in patch.bbclass - this works since that class
    # is inherited globally
    patches = bb.utils.exec_flat_python_func('src_patches', d)
    for patch in patches:
        _, _, local, _, _, parm = bb.fetch.decodeurl(patch)
        patchfiles.append(local)
    return patchfiles


def validate_pn(pn):
    """Perform validation on a recipe name (PN) for a new recipe."""
    reserved_names = ['forcevariable', 'append', 'prepend', 'remove']
    if not re.match('[0-9a-z-]+', pn):
        return 'Recipe name "%s" is invalid: only characters 0-9, a-z and - are allowed' % pn
    elif pn in reserved_names:
        return 'Recipe name "%s" is invalid: is a reserved keyword' % pn
    elif pn.startswith('pn-'):
        return 'Recipe name "%s" is invalid: names starting with "pn-" are reserved' % pn
    return ''

