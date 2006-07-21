inherit package
inherit rpm_core

RPMBUILD="rpmbuild --short-circuit ${RPMOPTS}"
PACKAGEFUNCS += "do_package_rpm"

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
	if not files:
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

#	fd = sys.__stdout__
	fd = specfile
	for var in out_vartranslate.keys():
		if out_vartranslate[var][0] == "%":
			continue
		fd.write("%s\t: %s\n" % (out_vartranslate[var], bb.data.getVar(var, d)))
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

	bb.data.setVar('BUILDSPEC', "${RPMBUILD} -bb ${OUTSPECFILE}\n", d)
	bb.data.setVarFlag('BUILDSPEC', 'func', '1', d)
	bb.build.exec_func('BUILDSPEC', d)

	# move the rpm into the pkgoutdir
	rpm = bb.data.expand('${RPMBUILDPATH}/RPMS/${TARGET_ARCH}/${PKG}-${PV}-${PR}.${TARGET_ARCH}.rpm', d)
	outrpm = bb.data.expand('${DEPLOY_DIR_RPM}/${PKG}-${PV}-${PR}.${TARGET_ARCH}.rpm', d)
	bb.movefile(rpm, outrpm)
}

python do_package_rpm () {
	workdir = bb.data.getVar('WORKDIR', d)
	if not workdir:
		raise bb.build.FuncFailed("WORKDIR not defined")
	workdir = bb.data.expand(workdir, d)

	import os # path manipulations
	outdir = bb.data.getVar('DEPLOY_DIR_RPM', d)
	if not outdir:
		raise bb.build.FuncFailed("DEPLOY_DIR_RPM not defined")
	outdir = bb.data.expand(outdir, d)
	bb.mkdirhier(outdir)

	packages = bb.data.getVar('PACKAGES', d)
	if not packages:
		packages = "${PN}"
		bb.data.setVar('FILES', '', d)
		ddir = bb.data.expand(bb.data.getVar('D', d), d)
		bb.mkdirhier(ddir)
		bb.data.setVar(bb.data.expand('FILES_${PN}', d), ''.join([ "./%s" % x for x in os.listdir(ddir)]), d)
	packages = bb.data.expand(packages, d)

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
		basedir = os.path.dirname(root)
		pkgoutdir = outdir
		bb.mkdirhier(pkgoutdir)
		bb.data.setVar('OUTSPECFILE', os.path.join(workdir, "%s.spec" % pkg), localdata)
		bb.build.exec_func('write_specfile', localdata)
		del localdata
}
