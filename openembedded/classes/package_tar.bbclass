inherit package

PACKAGEFUNCS += "do_package_tar"

python package_tar_fn () {
	import os
	from bb import data
	fn = os.path.join(bb.data.getVar('DEPLOY_DIR_TAR', d), "%s-%s-%s.tar.gz" % (bb.data.getVar('PKG', d), bb.data.getVar('PV', d), bb.data.getVar('PR', d)))
	fn = bb.data.expand(fn, d)
	bb.data.setVar('PKGFN', fn, d)
}

python package_tar_install () {
	import os, sys
	pkg = bb.data.getVar('PKG', d, 1)
	pkgfn = bb.data.getVar('PKGFN', d, 1)
	rootfs = bb.data.getVar('IMAGE_ROOTFS', d, 1)

	if None in (pkg,pkgfn,rootfs):
		bb.error("missing variables (one or more of PKG, PKGFN, IMAGEROOTFS)")
		raise bb.build.FuncFailed
	try:
		bb.mkdirhier(rootfs)
		os.chdir(rootfs)
	except OSError:
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
	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined, unable to package")
		return

	import os # path manipulations
	outdir = bb.data.getVar('DEPLOY_DIR_TAR', d, 1)
	if not outdir:
		bb.error("DEPLOY_DIR_TAR not defined, unable to package")
		return
	bb.mkdirhier(outdir)

	dvar = bb.data.getVar('D', d, 1)
	if not dvar:
		bb.error("D not defined, unable to package")
		return
	bb.mkdirhier(dvar)

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		bb.debug(1, "PACKAGES not defined, nothing to package")
		return

	for pkg in packages.split():
		localdata = bb.data.createCopy(d)
		root = "%s/install/%s" % (workdir, pkg)

		bb.data.setVar('ROOT', '', localdata)
		bb.data.setVar('ROOT_%s' % pkg, root, localdata)
		bb.data.setVar('PKG', pkg, localdata)

		overrides = bb.data.getVar('OVERRIDES', localdata)
		if not overrides:
			raise bb.build.FuncFailed('OVERRIDES not defined')
		overrides = bb.data.expand(overrides, localdata)
		bb.data.setVar('OVERRIDES', '%s:%s' % (overrides, pkg), localdata)

		bb.data.update_data(localdata)
# stuff
		root = bb.data.getVar('ROOT', localdata)
		bb.mkdirhier(root)
		basedir = os.path.dirname(root)
		pkgoutdir = outdir
		bb.mkdirhier(pkgoutdir)
		bb.build.exec_func('package_tar_fn', localdata)
		tarfn = bb.data.getVar('PKGFN', localdata, 1)
#		if os.path.exists(tarfn):
#			del localdata
#			continue
		os.chdir(root)
		from glob import glob
		if not glob('*'):
			bb.note("Not creating empty archive for %s-%s-%s" % (pkg, bb.data.getVar('PV', localdata, 1), bb.data.getVar('PR', localdata, 1)))
			continue
		ret = os.system("tar -czvf %s %s" % (tarfn, '.'))
		if ret != 0:
			bb.error("Creation of tar %s failed." % tarfn)
# end stuff
		del localdata
}
