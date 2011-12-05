# Deploy sources for recipes for compliance with copyleft-style licenses
# Defaults to using symlinks, as it's a quick operation, and one can easily
# follow the links when making use of the files (e.g. tar with the -h arg).
#
# By default, includes all GPL and LGPL, and excludes CLOSED and Proprietary.
#
# vi:sts=4:sw=4:et

COPYLEFT_SOURCES_DIR ?= '${DEPLOY_DIR}/copyleft_sources'

COPYLEFT_LICENSE_INCLUDE ?= 'GPL* LGPL*'
COPYLEFT_LICENSE_INCLUDE[type] = 'list'
COPYLEFT_LICENSE_INCLUDE[doc] = 'Space separated list of globs which include licenses'

COPYLEFT_LICENSE_EXCLUDE ?= 'CLOSED Proprietary'
COPYLEFT_LICENSE_EXCLUDE[type] = 'list'
COPYLEFT_LICENSE_INCLUDE[doc] = 'Space separated list of globs which exclude licenses'


def copyleft_should_include(d):
    """Determine if this recipe's sources should be deployed for compliance"""
    import ast
    import oe.license
    from fnmatch import fnmatchcase as fnmatch

    if oe.utils.inherits(d, 'native', 'nativesdk', 'cross', 'crossdk'):
        # not a target recipe
        return

    include = oe.data.typed_value('COPYLEFT_LICENSE_INCLUDE', d)
    exclude = oe.data.typed_value('COPYLEFT_LICENSE_EXCLUDE', d)

    def include_license(license):
        if any(fnmatch(license, pattern) for pattern in exclude):
            return False
        if any(fnmatch(license, pattern) for pattern in include):
            return True
        return False

    def choose_licenses(a, b):
        """Select the left option in an OR if all its licenses are to be included"""
        if all(include_license(lic) for lic in a):
            return a
        else:
            return b

    try:
        licenses = oe.license.flattened_licenses(d.getVar('LICENSE', True), choose_licenses)
    except oe.license.InvalidLicense as exc:
        bb.fatal('%s: %s' % (d.getVar('PF', True), exc))

    return all(include_license(lic) for lic in licenses)

python do_prepare_copyleft_sources () {
    """Populate a tree of the recipe sources and emit patch series files"""
    import os.path
    import shutil

    if not copyleft_should_include(d):
        return

    sources_dir = d.getVar('COPYLEFT_SOURCES_DIR', 1)
    src_uri = d.getVar('SRC_URI', 1).split()
    fetch = bb.fetch2.Fetch(src_uri, d)
    ud = fetch.ud

    locals = (fetch.localpath(url) for url in fetch.urls)
    localpaths = [local for local in locals if not local.endswith('.bb')]
    if not localpaths:
        return

    pf = d.getVar('PF', True)
    dest = os.path.join(sources_dir, pf)
    shutil.rmtree(dest, ignore_errors=True)
    bb.mkdirhier(dest)

    for path in localpaths:
        os.symlink(path, os.path.join(dest, os.path.basename(path)))

    patches = src_patches(d)
    for patch in patches:
        _, _, local, _, _, parm = bb.decodeurl(patch)
        patchdir = parm.get('patchdir')
        if patchdir:
            series = os.path.join(dest, 'series.subdir.%s' % patchdir.replace('/', '_'))
        else:
            series = os.path.join(dest, 'series')

        with open(series, 'a') as s:
            s.write('%s -p%s\n' % (os.path.basename(local), parm['striplevel']))
}

addtask prepare_copyleft_sources after do_fetch before do_build
do_build[recrdeptask] += 'do_prepare_copyleft_sources'
