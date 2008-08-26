inherit package

#IMAGE_PKGTYPE ?= "rpm"

RPMBUILD="rpmbuild --short-circuit ${RPMOPTS}"
IMAGE_PKGTYPE ?= "rpm"

RPMBUILDPATH="${WORKDIR}/rpm"

RPMOPTS="--rcfile=${WORKDIR}/rpmrc"
RPMOPTS="--rcfile=${WORKDIR}/rpmrc --target ${TARGET_SYS}"
RPM="rpm ${RPMOPTS}"

python write_specfile() {
	from bb import data, build
	import sys
	out_vartranslate = {
		"PKG": "Name",
		"PV": "Version",
		"PR": "Release",
		"DESCRIPTION": "%description",
		"ROOT": "BuildRoot",
		"LICENSE": "License",
		"SECTION": "Group",
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
		from bb import note
		note("Not creating empty archive for %s-%s-%s" % (bb.data.getVar('PKG',d, 1), bb.data.getVar('PV', d, 1), bb.data.getVar('PR', d, 1)))
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
		fd.write("%s\t: %s\n" % (out_vartranslate[var], val))

	fd.write("AutoReqProv: no\n")

	bb.build.exec_func("mapping_rename_hook", d)
	rdepends = " ".join(bb.utils.explode_deps(bb.data.getVar('RDEPENDS', d, True) or ""))
	if rdepends:
		fd.write("Requires: %s\n" % rdepends)
	fd.write("Summary\t: .\n")

	for var in out_vartranslate.keys():
		if out_vartranslate[var][0] != "%":
			continue
		fd.write(out_vartranslate[var] + "\n")
		fd.write(bb.data.getVar(var, d) + "\n\n")

	fd.write("%files\n")
	for file in files:
		fd.write("%s\n" % file)

	fd.close()

	# call out rpm -bb on the .spec, thereby creating an rpm

	bb.note(bb.data.expand("${RPMBUILD} -bb ${OUTSPECFILE}", d))

	bb.data.setVar('BUILDSPEC', "${RPMBUILD} -bb ${OUTSPECFILE}\n", d)
	bb.data.setVarFlag('BUILDSPEC', 'func', '1', d)
	bb.build.exec_func('BUILDSPEC', d)

	# move the rpm into the pkgoutdir
	rpm = bb.data.expand('${RPMBUILDPATH}/RPMS/${TARGET_ARCH}/${PKG}-${PV}-${PR}.${TARGET_ARCH}.rpm', d)
	outrpm = bb.data.expand('${DEPLOY_DIR_RPM}/${PKG}-${PV}-${PR}.${TARGET_ARCH}.rpm', d)
	bb.movefile(rpm, outrpm)
}


rpm_prep() {
	if [ ! -e ${WORKDIR}/rpmrc ]; then
		mkdir -p ${RPMBUILDPATH}/{SPECS,RPMS/{i386,i586,i686,noarch,ppc,mips,mipsel,arm},SRPMS,SOURCES,BUILD}
		echo 'macrofiles:/usr/lib/rpm/macros:${WORKDIR}/macros' > ${WORKDIR}/rpmrc
		echo '%_topdir ${RPMBUILDPATH}' > ${WORKDIR}/macros
		echo '%_repackage_dir ${WORKDIR}' >> ${WORKDIR}/macros
	fi
}

python do_package_rpm () {
	workdir = bb.data.getVar('WORKDIR', d, 1)
	if not workdir:
		bb.error("WORKDIR not defined, unable to package")
		return

	import os # path manipulations
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
		pkgoutdir = outdir
		bb.mkdirhier(pkgoutdir)
		bb.data.setVar('OUTSPECFILE', os.path.join(workdir, "%s.spec" % pkg), localdata)
		# Save the value of RPMBUILD expanded into the new dictonary so any 
		# changes in the compoents that make up workdir don't break packaging
		bb.data.setVar('RPMBUILD', bb.data.getVar("RPMBUILD", d, True), localdata)
		bb.build.exec_func('write_specfile', localdata)
		bb.utils.unlockfile(lf)
}

python () {
    import bb
    if bb.data.getVar('PACKAGES', d, True) != '':
        deps = (bb.data.getVarFlag('do_package_write_rpm', 'depends', d) or "").split()
        deps.append('rpm-native:do_populate_staging')
        deps.append('fakeroot-native:do_populate_staging')
        bb.data.setVarFlag('do_package_write_rpm', 'depends', " ".join(deps), d)
}


python do_package_write_rpm () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("rpm_prep", d)
	bb.build.exec_func("do_package_rpm", d)
}

do_package_write_rpm[dirs] = "${D}"
addtask package_write_rpm before do_package_write after do_package

