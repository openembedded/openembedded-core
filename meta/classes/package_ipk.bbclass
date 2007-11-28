inherit package

BOOTSTRAP_EXTRA_RDEPENDS += "ipkg-collateral ipkg ipkg-link"
DISTRO_EXTRA_RDEPENDS += "ipkg-collateral ipkg ipkg-link"
IMAGE_PKGTYPE ?= "ipk"

IPKGCONF_TARGET = "${STAGING_ETCDIR_NATIVE}/ipkg.conf"
IPKGCONF_SDK =  "${STAGING_ETCDIR_NATIVE}/ipkg-sdk.conf"

python package_ipk_fn () {
	from bb import data
	bb.data.setVar('PKGFN', bb.data.getVar('PKG',d), d)
}

python package_ipk_install () {
	import os, sys
	pkg = bb.data.getVar('PKG', d, 1)
	pkgfn = bb.data.getVar('PKGFN', d, 1)
	rootfs = bb.data.getVar('IMAGE_ROOTFS', d, 1)
	ipkdir = bb.data.getVar('DEPLOY_DIR_IPK', d, 1)
	stagingdir = bb.data.getVar('STAGING_DIR', d, 1)
	tmpdir = bb.data.getVar('TMPDIR', d, 1)

	if None in (pkg,pkgfn,rootfs):
		raise bb.build.FuncFailed("missing variables (one or more of PKG, PKGFN, IMAGEROOTFS)")
	try:
		bb.mkdirhier(rootfs)
		os.chdir(rootfs)
	except OSError:
		(type, value, traceback) = sys.exc_info()
		print value
		raise bb.build.FuncFailed

	# Generate ipk.conf if it or the stamp doesnt exist
	conffile = os.path.join(stagingdir,"ipkg.conf")
	if not os.access(conffile, os.R_OK):
		ipkg_archs = bb.data.getVar('PACKAGE_ARCHS',d)
		if ipkg_archs is None:
			bb.error("PACKAGE_ARCHS missing")
			raise FuncFailed
		ipkg_archs = ipkg_archs.split()
		arch_priority = 1

		f = open(conffile,"w")
		for arch in ipkg_archs:
			f.write("arch %s %s\n" % ( arch, arch_priority ))
			arch_priority += 1
		f.write("src local file:%s" % ipkdir)
		f.close()


	if (not os.access(os.path.join(ipkdir,"Packages"), os.R_OK) or
		not os.access(os.path.join(tmpdir, "stamps", "IPK_PACKAGE_INDEX_CLEAN"),os.R_OK):
		ret = os.system('ipkg-make-index -p %s %s ' % (os.path.join(ipkdir, "Packages"), ipkdir))
		if (ret != 0 ):
			raise bb.build.FuncFailed
		f = open(os.path.join(tmpdir, "stamps", "IPK_PACKAGE_INDEX_CLEAN"),"w")
		f.close()

	ret = os.system('ipkg-cl  -o %s -f %s update' % (rootfs, conffile))
	ret = os.system('ipkg-cl  -o %s -f %s install %s' % (rootfs, conffile, pkgfn))
	if (ret != 0 ):
		raise bb.build.FuncFailed
}

#
# Update the Packages index files in ${DEPLOY_DIR_IPK}
#
package_update_index_ipk () {
	set -x

	ipkgarchs="${PACKAGE_ARCHS}"

	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		return
	fi

	touch ${DEPLOY_DIR_IPK}/Packages
	ipkg-make-index -r ${DEPLOY_DIR_IPK}/Packages -p ${DEPLOY_DIR_IPK}/Packages -l ${DEPLOY_DIR_IPK}/Packages.filelist -m ${DEPLOY_DIR_IPK}

	for arch in $ipkgarchs; do
		if [ -e ${DEPLOY_DIR_IPK}/$arch/ ] ; then 
			touch ${DEPLOY_DIR_IPK}/$arch/Packages
			ipkg-make-index -r ${DEPLOY_DIR_IPK}/$arch/Packages -p ${DEPLOY_DIR_IPK}/$arch/Packages -l ${DEPLOY_DIR_IPK}/$arch/Packages.filelist -m ${DEPLOY_DIR_IPK}/$arch/
		fi
		if [ -e ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/ ] ; then 
			touch ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/Packages
			ipkg-make-index -r ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/Packages -p ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/Packages -l ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/Packages.filelist -m ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/
		fi
	done
}

#
# Generate an ipkg conf file ${IPKGCONF_TARGET} suitable for use against 
# the target system and an ipkg conf file ${IPKGCONF_SDK} suitable for 
# use against the host system in sdk builds
#
package_generate_ipkg_conf () {
	mkdir -p ${STAGING_ETCDIR_NATIVE}/
	echo "src oe file:${DEPLOY_DIR_IPK}" > ${IPKGCONF_TARGET}
	echo "src oe file:${DEPLOY_DIR_IPK}" > ${IPKGCONF_SDK}
	ipkgarchs="${PACKAGE_ARCHS}"
	priority=1
	for arch in $ipkgarchs; do
		echo "arch $arch $priority" >> ${IPKGCONF_TARGET}
		echo "arch ${BUILD_ARCH}-$arch-sdk $priority" >> ${IPKGCONF_SDK}
		priority=$(expr $priority + 5)
		if [ -e ${DEPLOY_DIR_IPK}/$arch/Packages ] ; then
		        echo "src oe-$arch file:${DEPLOY_DIR_IPK}/$arch" >> ${IPKGCONF_TARGET}
		fi
		if [ -e ${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk/Packages ] ; then
		        echo "src oe-${BUILD_ARCH}-$arch-sdk file:${DEPLOY_DIR_IPK}/${BUILD_ARCH}-$arch-sdk" >> ${IPKGCONF_SDK}
		fi
	done
}

python do_package_ipk () {
	import sys, re, copy

	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined, unable to package")
		return

	import os # path manipulations
	outdir = bb.data.getVar('DEPLOY_DIR_IPK', d, 1)
	if not outdir:
		bb.error("DEPLOY_DIR_IPK not defined, unable to package")
		return

	dvar = bb.data.getVar('D', d, 1)
	if not dvar:
		bb.error("D not defined, unable to package")
		return
	bb.mkdirhier(dvar)

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		bb.debug(1, "PACKAGES not defined, nothing to package")
		return

	tmpdir = bb.data.getVar('TMPDIR', d, 1)

	if os.access(os.path.join(tmpdir, "stamps", "IPK_PACKAGE_INDEX_CLEAN"), os.R_OK):
		os.unlink(os.path.join(tmpdir, "stamps", "IPK_PACKAGE_INDEX_CLEAN"))

	if packages == []:
		bb.debug(1, "No packages; nothing to do")
		return

	for pkg in packages.split():
		localdata = bb.data.createCopy(d)
		pkgdest = bb.data.getVar('PKGDEST', d, 1)
		root = "%s/%s" % (pkgdest, pkg)

		lf = bb.utils.lockfile(root + ".lock")

		bb.data.setVar('ROOT', '', localdata)
		bb.data.setVar('ROOT_%s' % pkg, root, localdata)
		pkgname = bb.data.getVar('PKG_%s' % pkg, localdata, 1)
		if not pkgname:
			pkgname = pkg
		bb.data.setVar('PKG', pkgname, localdata)

		overrides = bb.data.getVar('OVERRIDES', localdata)
		if not overrides:
			raise bb.build.FuncFailed('OVERRIDES not defined')
		overrides = bb.data.expand(overrides, localdata)
		bb.data.setVar('OVERRIDES', overrides + ':' + pkg, localdata)

		bb.data.update_data(localdata)
		basedir = os.path.join(os.path.dirname(root))
		arch = bb.data.getVar('PACKAGE_ARCH', localdata, 1)
		pkgoutdir = "%s/%s" % (outdir, arch)
		bb.mkdirhier(pkgoutdir)
		os.chdir(root)
		from glob import glob
		g = glob('*')
		try:
			del g[g.index('CONTROL')]
			del g[g.index('./CONTROL')]
		except ValueError:
			pass
		if not g and bb.data.getVar('ALLOW_EMPTY', localdata) != "1":
			from bb import note
			note("Not creating empty archive for %s-%s-%s" % (pkg, bb.data.getVar('PV', localdata, 1), bb.data.getVar('PR', localdata, 1)))
			bb.utils.unlockfile(lf)
			continue

		controldir = os.path.join(root, 'CONTROL')
		bb.mkdirhier(controldir)
		try:
			ctrlfile = file(os.path.join(controldir, 'control'), 'w')
		except OSError:
			bb.utils.unlockfile(lf)
			raise bb.build.FuncFailed("unable to open control file for writing.")

		fields = []
		pe = bb.data.getVar('PE', d, 1)
		if pe and int(pe) > 0:
			fields.append(["Version: %s:%s-%s\n", ['PE', 'PV', 'PR']])
		else:
			fields.append(["Version: %s-%s\n", ['PV', 'PR']])
		fields.append(["Description: %s\n", ['DESCRIPTION']])
		fields.append(["Section: %s\n", ['SECTION']])
		fields.append(["Priority: %s\n", ['PRIORITY']])
		fields.append(["Maintainer: %s\n", ['MAINTAINER']])
		fields.append(["Architecture: %s\n", ['PACKAGE_ARCH']])
		fields.append(["OE: %s\n", ['PN']])
		fields.append(["Homepage: %s\n", ['HOMEPAGE']])

		def pullData(l, d):
			l2 = []
			for i in l:
				l2.append(bb.data.getVar(i, d, 1))
			return l2

		ctrlfile.write("Package: %s\n" % pkgname)
		# check for required fields
		try:
			for (c, fs) in fields:
				for f in fs:
					if bb.data.getVar(f, localdata) is None:
						raise KeyError(f)
				ctrlfile.write(c % tuple(pullData(fs, localdata)))
		except KeyError:
			(type, value, traceback) = sys.exc_info()
			ctrlfile.close()
			bb.utils.unlockfile(lf)
			raise bb.build.FuncFailed("Missing field for ipk generation: %s" % value)
		# more fields

		bb.build.exec_func("mapping_rename_hook", localdata)

		rdepends = explode_deps(bb.data.getVar("RDEPENDS", localdata, 1) or "")
		rrecommends = explode_deps(bb.data.getVar("RRECOMMENDS", localdata, 1) or "")
		rsuggests = (bb.data.getVar("RSUGGESTS", localdata, 1) or "").split()
		rprovides = (bb.data.getVar("RPROVIDES", localdata, 1) or "").split()
		rreplaces = (bb.data.getVar("RREPLACES", localdata, 1) or "").split()
		rconflicts = (bb.data.getVar("RCONFLICTS", localdata, 1) or "").split()
		if rdepends:
			ctrlfile.write("Depends: %s\n" % ", ".join(rdepends))
		if rsuggests:
			ctrlfile.write("Suggests: %s\n" % ", ".join(rsuggests))
		if rrecommends:
			ctrlfile.write("Recommends: %s\n" % ", ".join(rrecommends))
		if rprovides:
			ctrlfile.write("Provides: %s\n" % ", ".join(rprovides))
		if rreplaces:
			ctrlfile.write("Replaces: %s\n" % ", ".join(rreplaces))
		if rconflicts:
			ctrlfile.write("Conflicts: %s\n" % ", ".join(rconflicts))
		src_uri = bb.data.getVar("SRC_URI", localdata, 1)
		if src_uri:
			src_uri = re.sub("\s+", " ", src_uri)
			ctrlfile.write("Source: %s\n" % " ".join(src_uri.split()))
		ctrlfile.close()

		for script in ["preinst", "postinst", "prerm", "postrm"]:
			scriptvar = bb.data.getVar('pkg_%s' % script, localdata, 1)
			if not scriptvar:
				continue
			try:
				scriptfile = file(os.path.join(controldir, script), 'w')
			except OSError:
				bb.utils.unlockfile(lf)
				raise bb.build.FuncFailed("unable to open %s script file for writing." % script)
			scriptfile.write(scriptvar)
			scriptfile.close()
			os.chmod(os.path.join(controldir, script), 0755)

		conffiles_str = bb.data.getVar("CONFFILES", localdata, 1)
		if conffiles_str:
			try:
				conffiles = file(os.path.join(controldir, 'conffiles'), 'w')
			except OSError:
				bb.utils.unlockfile(lf)
				raise bb.build.FuncFailed("unable to open conffiles for writing.")
			for f in conffiles_str.split():
				conffiles.write('%s\n' % f)
			conffiles.close()

		os.chdir(basedir)
		ret = os.system("PATH=\"%s\" %s %s %s" % (bb.data.getVar("PATH", localdata, 1), 
                                                          bb.data.getVar("IPKGBUILDCMD",d,1), pkg, pkgoutdir))
		if ret != 0:
			bb.utils.unlockfile(lf)
			raise bb.build.FuncFailed("ipkg-build execution failed")

		for script in ["preinst", "postinst", "prerm", "postrm", "control" ]:
			scriptfile = os.path.join(controldir, script)
			try:
				os.remove(scriptfile)
			except OSError:
				pass
		try:
			os.rmdir(controldir)
		except OSError:
			pass
		bb.utils.unlockfile(lf)
}

python () {
    import bb
    if bb.data.getVar('PACKAGES', d, True) != '':
        deps = (bb.data.getVarFlag('do_package_write_ipk', 'depends', d) or "").split()
        deps.append('ipkg-utils-native:do_populate_staging')
        deps.append('fakeroot-native:do_populate_staging')
        bb.data.setVarFlag('do_package_write_ipk', 'depends', " ".join(deps), d)
}

python do_package_write_ipk () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("do_package_ipk", d)
}
do_package_write_ipk[dirs] = "${D}"
addtask package_write_ipk before do_package_write after do_package
