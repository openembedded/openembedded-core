inherit package

IMAGE_PKGTYPE ?= "tar"

python package_tar_fn () {
	fn = os.path.join(d.getVar('DEPLOY_DIR_TAR'), "%s-%s-%s.tar.gz" % (d.getVar('PKG'), d.getVar('PKGV'), d.getVar('PKGR')))
	fn = d.expand(fn)
	d.setVar('PKGFN', fn)
}

python package_tar_install () {
	pkg = d.getVar('PKG', True)
	pkgfn = d.getVar('PKGFN', True)
	rootfs = d.getVar('IMAGE_ROOTFS', True)

	if None in (pkg,pkgfn,rootfs):
		bb.error("missing variables (one or more of PKG, PKGFN, IMAGEROOTFS)")
		raise bb.build.FuncFailed
	try:
		bb.mkdirhier(rootfs)
		os.chdir(rootfs)
	except OSError:
		import sys
		(type, value, traceback) = sys.exc_info()
		print value
		raise bb.build.FuncFailed

	if not os.access(pkgfn, os.R_OK):
		bb.debug(1, "%s does not exist, skipping" % pkgfn)
		raise bb.build.FuncFailed

	ret = os.system('zcat %s | tar -xf -' % pkgfn)
	if ret != 0:
		raise bb.build.FuncFailed
}

python do_package_tar () {
	workdir = d.getVar('WORKDIR', True)
	if not workdir:
		bb.error("WORKDIR not defined, unable to package")
		return

	outdir = d.getVar('DEPLOY_DIR_TAR', True)
	if not outdir:
		bb.error("DEPLOY_DIR_TAR not defined, unable to package")
		return
	bb.mkdirhier(outdir)

	dvar = d.getVar('D', True)
	if not dvar:
		bb.error("D not defined, unable to package")
		return
	bb.mkdirhier(dvar)

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
		bb.mkdirhier(root)
		basedir = os.path.dirname(root)
		pkgoutdir = outdir
		bb.mkdirhier(pkgoutdir)
		bb.build.exec_func('package_tar_fn', localdata)
		tarfn = localdata.getVar('PKGFN', True)
		os.chdir(root)
		from glob import glob
		if not glob('*'):
			bb.note("Not creating empty archive for %s-%s-%s" % (pkg, localdata.getVar('PKGV', True), localdata.getVar('PKGR', True)))
			continue
		ret = os.system("tar -czf %s %s" % (tarfn, '.'))
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
addtask package_write_tar before do_build after do_package
