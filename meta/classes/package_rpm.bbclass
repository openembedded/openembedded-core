inherit package

#IMAGE_PKGTYPE ?= "rpm"

RPMBUILD="rpmbuild --short-circuit ${RPMOPTS}"
IMAGE_PKGTYPE ?= "rpm"

RPMBUILDPATH="${WORKDIR}/rpm"

RPMOPTS="--rcfile=${WORKDIR}/rpmrc"
RPMOPTS="--rcfile=${WORKDIR}/rpmrc --target ${TARGET_SYS}"
RPM="rpm ${RPMOPTS}"

python write_specfile() {
	version = bb.data.getVar('PV', d, 1)
	version = version.replace('-', '+')
	bb.data.setVar('RPMPV', version, d)

	out_vartranslate = {
		"PKG": "Name",
		"RPMPV": "Version",
		"PR": "Release",
		"DESCRIPTION": "%description",
		"ROOT": "BuildRoot",
		"LICENSE": "License",
		"SECTION": "Group",
		"pkg_postinst": "%post",
		"pkg_preinst": "%pre",
	}

	root = bb.data.getVar('ROOT', d)

	# get %files
	filesvar = bb.data.expand(bb.data.getVar('FILES', d), d) or ""
	from glob import glob
	files = filesvar.split()
	todelete = []
	for file in files:
		if file[0] == '.':
			newfile = file[1:]
			files[files.index(file)] = newfile
			file = newfile
		else:
			newfile = file
		realfile = os.path.join(root, './'+file)
		if not glob(realfile):
			todelete.append(files[files.index(newfile)])
	for r in todelete:
		try:
			del files[files.index(r)]
		except ValueError:
			pass

	if not files and bb.data.getVar('ALLOW_EMPTY', d) != "1":
		bb.note("Not creating empty archive for %s-%s-%s" % (bb.data.getVar('PKG',d, 1), bb.data.getVar('PV', d, 1), bb.data.getVar('PR', d, 1)))
		return

	# output .spec using this metadata store
	try:
		from __builtin__ import file
		if not bb.data.getVar('OUTSPECFILE', d):
			raise OSError('eek!')
		specfile = file(bb.data.getVar('OUTSPECFILE', d), 'w')
	except OSError:
		raise bb.build.FuncFailed("unable to open spec file for writing.")

	fd = specfile
	for var in out_vartranslate.keys():
		if out_vartranslate[var][0] == "%":
			continue
		val = bb.data.getVar(var, d, 1)
		if val:
			fd.write("%s\t: %s\n" % (out_vartranslate[var], val))

	fd.write("AutoReqProv: no\n")

	def fix_dep_versions(varname):
		depends = bb.utils.explode_dep_versions(bb.data.getVar(varname, d, True) or "")
		newdeps = []
		for dep in depends:
			ver = depends[dep]
			if dep and ver:
				if '-' in ver:
					subd = read_subpkgdata_dict(dep, d)
					pv = subd['PV']
					reppv = pv.replace('-', '+')
					ver = ver.replace(pv, reppv)
				newdeps.append("%s (%s)" % (dep, ver))
			elif dep:
				newdeps.append(dep)
		bb.data.setVar(varname, " ".join(newdeps), d)

	fix_dep_versions('RDEPENDS')
	fix_dep_versions('RRECOMMENDS')

	bb.build.exec_func("mapping_rename_hook", d)

	def write_dep_field(varname, outstring):
		depends = bb.utils.explode_dep_versions(bb.data.getVar(varname, d, True) or "")
		for dep in depends:
			ver = depends[dep]
			if dep and ver:
				fd.write("%s: %s %s\n" % (outstring, dep, ver))
			elif dep:
				fd.write("%s: %s\n" % (outstring, dep))

	write_dep_field('RDEPENDS', 'Requires')
	write_dep_field('RRECOMMENDS', 'Recommends')

	fd.write("Summary\t: .\n")

	for var in out_vartranslate.keys():
		if out_vartranslate[var][0] != "%":
			continue
		val = bb.data.getVar(var, d)
		if val:
			fd.write(out_vartranslate[var] + "\n")
			fd.write(val + "\n\n")

	fd.write("%files\n")
	for file in files:
		if file[0] != '/':
			fd.write('/')
		fd.write("%s\n" % file)

	fd.close()

	# call out rpm -bb on the .spec, thereby creating an rpm

	bb.note(bb.data.expand("${RPMBUILD} -bb ${OUTSPECFILE}", d))

	bb.data.setVar('BUILDSPEC', "${RPMBUILD} -bb ${OUTSPECFILE}\n", d)
	bb.data.setVarFlag('BUILDSPEC', 'func', '1', d)
	bb.build.exec_func('BUILDSPEC', d)

	# move the rpm into the pkgoutdir
	rpm = bb.data.expand('${RPMBUILDPATH}/RPMS/${TARGET_ARCH}/${PKG}-${RPMPV}-${PR}.${TARGET_ARCH}.rpm', d)
	outrpm = bb.data.expand('${DEPLOY_DIR_RPM}/${PACKAGE_ARCH}/${PKG}-${RPMPV}-${PR}.${TARGET_ARCH}.rpm', d)
	bb.movefile(rpm, outrpm)
}


rpm_prep() {
	if [ ! -e ${WORKDIR}/rpmrc ]; then
		mkdir -p ${RPMBUILDPATH}/{SPECS,RPMS/{i386,i586,i686,noarch,ppc,mips,mipsel,arm},SRPMS,SOURCES,BUILD}
		echo 'macrofiles:${STAGING_DIR_NATIVE}/usr/lib/rpm/macros:${WORKDIR}/macros' > ${WORKDIR}/rpmrc
		echo '%_topdir ${RPMBUILDPATH}' > ${WORKDIR}/macros
		echo '%_repackage_dir ${WORKDIR}' >> ${WORKDIR}/macros
	fi
}

python do_package_rpm () {
	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined, unable to package")
		return

	outdir = bb.data.getVar('DEPLOY_DIR_RPM', d, 1)
	if not outdir:
		bb.error("DEPLOY_DIR_RPM not defined, unable to package")
		return
	bb.mkdirhier(outdir)

	packages = bb.data.getVar('PACKAGES', d, 1)
	if not packages:
		bb.debug(1, "PACKAGES not defined, nothing to package")
		return

	if packages == []:
		bb.debug(1, "No packages; nothing to do")
		return

	# If "rpm" comes into overrides the presence of this function causes problems.
	# Since we don't need it, remove it for now - hacky.
	bb.data.delVar("do_package_write_rpm", d)

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
	        pkgoutdir = os.path.join(outdir, bb.data.getVar('PACKAGE_ARCH', localdata, 1))
		bb.mkdirhier(pkgoutdir)
		bb.data.setVar('OUTSPECFILE', os.path.join(workdir, "%s.spec" % pkg), localdata)
		# Save the value of RPMBUILD expanded into the new dictonary so any 
		# changes in the compoents that make up workdir don't break packaging
		bb.data.setVar('RPMBUILD', bb.data.getVar("RPMBUILD", d, True), localdata)
		bb.data.setVar('RPMBUILDPATH', bb.data.getVar("RPMBUILDPATH", d, True), localdata)
		bb.build.exec_func('write_specfile', localdata)
		bb.utils.unlockfile(lf)
}

python () {
    if bb.data.getVar('PACKAGES', d, True) != '':
        deps = (bb.data.getVarFlag('do_package_write_rpm', 'depends', d) or "").split()
        deps.append('rpm-native:do_populate_sysroot')
        deps.append('fakeroot-native:do_populate_sysroot')
        bb.data.setVarFlag('do_package_write_rpm', 'depends', " ".join(deps), d)
}


python do_package_write_rpm () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("rpm_prep", d)
	bb.build.exec_func("do_package_rpm", d)
}

do_package_write_rpm[dirs] = "${D}"
addtask package_write_rpm before do_package_write after do_package

