inherit package

IMAGE_PKGTYPE ?= "tar"

python package_tar_fn () {
    fn = os.path.join(d.getVar('DEPLOY_DIR_TAR'), "%s-%s-%s.tar.gz" % (d.getVar('PKG'), d.getVar('PKGV'), d.getVar('PKGR')))
    fn = d.expand(fn)
    d.setVar('PKGFN', fn)
}

python do_package_tar () {
    import subprocess
    workdir = d.getVar('WORKDIR', True)
    if not workdir:
        bb.error("WORKDIR not defined, unable to package")
        return

    outdir = d.getVar('DEPLOY_DIR_TAR', True)
    if not outdir:
        bb.error("DEPLOY_DIR_TAR not defined, unable to package")
        return
    bb.utils.mkdirhier(outdir)

    dvar = d.getVar('D', True)
    if not dvar:
        bb.error("D not defined, unable to package")
        return
    bb.utils.mkdirhier(dvar)

    packages = d.getVar('PACKAGES', True)
    if not packages:
        bb.debug(1, "PACKAGES not defined, nothing to package")
        return

    for pkg in packages.split():
        localdata = bb.data.createCopy(d)
        root = "%s/install/%s" % (workdir, pkg)

        localdata.setVar('ROOT', '')
        localdata.setVar('ROOT_%s' % pkg, root)
        localdata.setVar('PKG', pkg)

        overrides = localdata.getVar('OVERRIDES')
        if not overrides:
            raise bb.build.FuncFailed('OVERRIDES not defined')
        overrides = localdata.expand(overrides)
        localdata.setVar('OVERRIDES', '%s:%s' % (overrides, pkg))

        bb.data.update_data(localdata)

        root = localdata.getVar('ROOT')
        bb.utils.mkdirhier(root)
        basedir = os.path.dirname(root)
        pkgoutdir = outdir
        bb.utils.mkdirhier(pkgoutdir)
        bb.build.exec_func('package_tar_fn', localdata)
        tarfn = localdata.getVar('PKGFN', True)
        os.chdir(root)
        from glob import glob
        if not glob('*'):
            bb.note("Not creating empty archive for %s-%s-%s" % (pkg, localdata.getVar('PKGV', True), localdata.getVar('PKGR', True)))
            continue
        ret = subprocess.call("tar -czf %s %s" % (tarfn, '.'), shell=True)
        if ret != 0:
            bb.error("Creation of tar %s failed." % tarfn)
}

python () {
    if d.getVar('PACKAGES', True) != '':
        deps = (d.getVarFlag('do_package_write_tar', 'depends') or "").split()
        deps.append('tar-native:do_populate_sysroot')
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        d.setVarFlag('do_package_write_tar', 'depends', " ".join(deps))
        d.setVarFlag('do_package_write_ipk', 'fakeroot', "1")
}


python do_package_write_tar () {
    bb.build.exec_func("read_subpackage_metadata", d)
    bb.build.exec_func("do_package_tar", d)
}
do_package_write_tar[dirs] = "${D}"
addtask package_write_tar before do_build after do_packagedata do_package
