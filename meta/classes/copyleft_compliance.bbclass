# Deploy sources for recipes for compliance with copyleft-style licenses
# Defaults to using symlinks, as it's a quick operation, and one can easily
# follow the links when making use of the files (e.g. tar with the -h arg).
#
# By default, includes all GPL and LGPL, and excludes CLOSED and Proprietary.
#
# vi:sts=4:sw=4:et

# Need the copyleft_should_include
inherit archiver

COPYLEFT_SOURCES_DIR ?= '${DEPLOY_DIR}/copyleft_sources'

python do_prepare_copyleft_sources () {
    """Populate a tree of the recipe sources and emit patch series files"""
    import shutil

    p = d.getVar('P', True)
    included, reason = copyleft_should_include(d)
    if not included:
        bb.debug(1, 'copyleft: %s is excluded: %s' % (p, reason))
        return
    else:
        bb.debug(1, 'copyleft: %s is included: %s' % (p, reason))

    sources_dir = d.getVar('COPYLEFT_SOURCES_DIR', True)
    src_uri = d.getVar('SRC_URI', True).split()
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
