# Deploy sources for recipes for compliance with copyleft-style licenses
# Defaults to using symlinks, as it's a quick operation, and one can easily
# follow the links when making use of the files (e.g. tar with the -h arg).
#
# By default, includes all GPL and LGPL, and excludes CLOSED and Proprietary.
#
# vi:sts=4:sw=4:et

COPYLEFT_LICENSE_INCLUDE ?= 'GPL* LGPL*'
COPYLEFT_LICENSE_INCLUDE[type] = 'list'
COPYLEFT_LICENSE_INCLUDE[doc] = 'Space separated list of globs which include licenses'

COPYLEFT_LICENSE_EXCLUDE ?= 'CLOSED Proprietary'
COPYLEFT_LICENSE_EXCLUDE[type] = 'list'
COPYLEFT_LICENSE_EXCLUDE[doc] = 'Space separated list of globs which exclude licenses'

COPYLEFT_RECIPE_TYPE ?= '${@copyleft_recipe_type(d)}'
COPYLEFT_RECIPE_TYPE[doc] = 'The "type" of the current recipe (e.g. target, native, cross)'

COPYLEFT_RECIPE_TYPES ?= 'target'
COPYLEFT_RECIPE_TYPES[type] = 'list'
COPYLEFT_RECIPE_TYPES[doc] = 'Space separated list of recipe types to include'

COPYLEFT_AVAILABLE_RECIPE_TYPES = 'target native nativesdk cross crosssdk cross-canadian'
COPYLEFT_AVAILABLE_RECIPE_TYPES[type] = 'list'
COPYLEFT_AVAILABLE_RECIPE_TYPES[doc] = 'Space separated list of available recipe types'

COPYLEFT_SOURCES_DIR ?= '${DEPLOY_DIR}/copyleft_sources'

def copyleft_recipe_type(d):
    for recipe_type in oe.data.typed_value('COPYLEFT_AVAILABLE_RECIPE_TYPES', d):
        if oe.utils.inherits(d, recipe_type):
            return recipe_type
    return 'target'

def copyleft_should_include(d):
    """
    Determine if this recipe's sources should be deployed for compliance
    """
    import ast
    import oe.license
    from fnmatch import fnmatchcase as fnmatch

    recipe_type = d.getVar('COPYLEFT_RECIPE_TYPE', True)
    if recipe_type not in oe.data.typed_value('COPYLEFT_RECIPE_TYPES', d):
        return False, 'recipe type "%s" is excluded' % recipe_type

    include = oe.data.typed_value('COPYLEFT_LICENSE_INCLUDE', d)
    exclude = oe.data.typed_value('COPYLEFT_LICENSE_EXCLUDE', d)

    try:
        is_included, reason = oe.license.is_included(d.getVar('LICENSE', True), include, exclude)
    except oe.license.LicenseError as exc:
        bb.fatal('%s: %s' % (d.getVar('PF', True), exc))
    else:
        if is_included:
            if reason:
                return True, 'recipe has included licenses: %s' % ', '.join(reason)
            else:
                return False, 'recipe does not include a copyleft license'
        else:
            return False, 'recipe has excluded licenses: %s' % ', '.join(reason)


python do_prepare_copyleft_sources () {
    """Populate a tree of the recipe sources and emit patch series files"""
    import os.path
    import shutil

    p = d.getVar('P', True)
    included, reason = copyleft_should_include(d)
    if not included:
        bb.debug(1, 'copyleft: %s is excluded: %s' % (p, reason))
        return
    else:
        bb.debug(1, 'copyleft: %s is included: %s' % (p, reason))

    sources_dir = d.getVar('COPYLEFT_SOURCES_DIR', True)
    dl_dir = d.getVar('DL_DIR', True)
    src_uri = d.getVar('SRC_URI', True).split()
    fetch = bb.fetch2.Fetch(src_uri, d)
    ud = fetch.ud

    pf = d.getVar('PF', True)
    dest = os.path.join(sources_dir, pf)
    shutil.rmtree(dest, ignore_errors=True)
    bb.utils.mkdirhier(dest)

    for u in ud.values():
        local = os.path.normpath(fetch.localpath(u.url))
        if local.endswith('.bb'):
            continue
        elif local.endswith('/'):
            local = local[:-1]

        if u.mirrortarball:
            tarball_path = os.path.join(dl_dir, u.mirrortarball)
            if os.path.exists(tarball_path):
                local = tarball_path

        oe.path.symlink(local, os.path.join(dest, os.path.basename(local)), force=True)

    patches = src_patches(d)
    for patch in patches:
        _, _, local, _, _, parm = bb.fetch.decodeurl(patch)
        patchdir = parm.get('patchdir')
        if patchdir:
            series = os.path.join(dest, 'series.subdir.%s' % patchdir.replace('/', '_'))
        else:
            series = os.path.join(dest, 'series')

        with open(series, 'a') as s:
            s.write('%s -p%s\n' % (os.path.basename(local), parm['striplevel']))
}

addtask prepare_copyleft_sources after do_fetch before do_build
do_prepare_copyleft_sources[dirs] = "${WORKDIR}"
do_build[recrdeptask] += 'do_prepare_copyleft_sources'
