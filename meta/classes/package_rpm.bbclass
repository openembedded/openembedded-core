inherit package

IMAGE_PKGTYPE ?= "rpm"

RPM="${BUILD_ARCH}-${BUILD_OS}-rpm"
RPMBUILD="${BUILD_ARCH}-${BUILD_OS}-rpmbuild"

PKGWRITEDIRRPM = "${WORKDIR}/deploy-rpms"

python package_rpm_fn () {
	bb.data.setVar('PKGFN', bb.data.getVar('PKG',d), d)
}

python package_rpm_install () {
	bb.fatal("package_rpm_install not implemented!")
}

#
# Update the Packages depsolver db in ${DEPLOY_DIR_RPM}
#
package_update_index_rpm () {
	rpmarchs="${PACKAGE_ARCHS}"

	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		return
	fi

	packagedirs=""
	for arch in $rpmarchs ; do
		sdkarch=`echo $arch | sed -e 's/${HOST_ARCH}/${SDK_ARCH}/'`
		packagedirs="$packagedirs ${DEPLOY_DIR_RPM}/$arch ${DEPLOY_DIR_RPM}/$sdkarch-nativesdk"
	done

	packagedirs="$packagedirs ${DEPLOY_DIR_RPM}/${SDK_ARCH}-${TARGET_ARCH}-canadian"

	cat /dev/null > ${DEPLOY_DIR_RPM}/solvedb.conf
	for pkgdir in $packagedirs; do
		if [ -e $pkgdir/ ]; then
			rm -rf $pkgdir/solvedb
			mkdir -p $pkgdir/solvedb
			echo "# Dynamically generated solve manifest" >> $pkgdir/solvedb/manifest
			find $pkgdir -maxdepth 1 -type f >> $pkgdir/solvedb/manifest
			${RPM} -i --replacepkgs --replacefiles --oldpackage \
				-D "_dbpath $pkgdir/solvedb" --justdb \
				--noaid --nodeps --noorder --noscripts --notriggers --noparentdirs --nolinktos --stats \
				$pkgdir/solvedb/manifest
			echo $pkgdir/solvedb >> ${DEPLOY_DIR_RPM}/solvedb.conf
		fi
	done
}

#
# Generate an rpm configuration suitable for use against the
# generated depsolver db's...
#
package_generate_rpm_conf () {
	printf "_solve_dbpath " > ${DEPLOY_DIR_RPM}/solvedb.macro
	colon=false
	for each in `cat ${DEPLOY_DIR_RPM}/solvedb.conf` ; do
		if [ "$colon" == true ]; then
			printf ":" >> ${DEPLOY_DIR_RPM}/solvedb.macro
		fi
		printf "%s" $each >> ${DEPLOY_DIR_RPM}/solvedb.macro
		colon=true
	done
	printf "\n" >> ${DEPLOY_DIR_RPM}/solvedb.macro
}

python write_specfile () {
	# We need to change '-' in a version field to '+'
	# This needs to be done BEFORE the mapping_rename_hook
	def translate_vers(varname, d):
		depends = bb.data.getVar(varname, d, True)
		if depends:
			depends_dict = bb.utils.explode_dep_versions(depends)
			newdeps_dict = {}
			for dep in depends_dict:
				ver = depends_dict[dep]
				if dep and ver:
					if '-' in ver:
						subd = read_subpkgdata_dict(dep, d)
						pv = subd['PV']
						reppv = pv.replace('-', '+')
						ver = ver.replace(pv, reppv)
				newdeps_dict[dep] = ver
			depends = bb.utils.join_deps(newdeps_dict)
			bb.data.setVar(varname, depends.strip(), d)

	# We need to change the style the dependency from BB to RPM
	# This needs to happen AFTER the mapping_rename_hook
	def translate_deps(varname, d):
		depends = bb.data.getVar('RPM' + varname, d, True) or bb.data.getVar(varname, d, True) or ""
		depends = depends.replace('(', '')
		depends = depends.replace(')', '')
		bb.data.setVar('RPM' + varname, depends, d)

	def walk_files(walkpath, target, conffiles):
		import os
		for rootpath, dirs, files in os.walk(walkpath):
			path = rootpath.replace(walkpath, "")
			#for dir in dirs:
			#	target.append("%dir " + path + "/" + dir)
			for file in files:
				if conffiles.count(path + "/" + file):
					target.append("%config " + path + "/" + file)
				else:
					target.append(path + "/" + file)

	packages = bb.data.getVar('PACKAGES', d, True)
	if not packages or packages == '':
		bb.debug(1, "No packages; nothing to do")
		return

	pkgdest = bb.data.getVar('PKGDEST', d, True)
	if not pkgdest:
		bb.fatal("No PKGDEST")
		return

	outspecfile = bb.data.getVar('OUTSPECFILE', d, True)
	if not outspecfile:
		bb.fatal("No OUTSPECFILE")
		return

	# Construct the SPEC file...
	srcname    = bb.data.getVar('PN', d, True)
	srcsummary = (bb.data.getVar('SUMMARY', d, True) or ".")
	srcversion = bb.data.getVar('PV', d, True).replace('-', '+')
	srcrelease = bb.data.getVar('PR', d, True)
	srcepoch   = (bb.data.getVar('PE', d, True) or "")
	srclicense = bb.data.getVar('LICENSE', d, True)
	srcsection = bb.data.getVar('SECTION', d, True)
	srcmaintainer  = bb.data.getVar('MAINTAINER', d, True)
	srchomepage    = bb.data.getVar('HOMEPAGE', d, True)
	srcdescription = bb.data.getVar('DESCRIPTION', d, True)

	translate_deps('DEPENDS', d)
	srcdepends     = bb.data.getVar('RPMDEPENDS', d, True)
	srcrdepends    = []
	srcrrecommends = []
	srcrsuggests   = []
	srcrprovides   = []
	srcrreplaces   = []
	srcrconflicts  = []
	srcrobsoletes  = []

	srcpreinst  = []
	srcpostinst = []
	srcprerm    = []
	srcpostrm   = []

	spec_preamble_top = []
	spec_preamble_bottom = []

	spec_scriptlets_top = []
	spec_scriptlets_bottom = []

	spec_files_top = []
	spec_files_bottom = []

	for pkg in packages.split():
		localdata = bb.data.createCopy(d)

		root = "%s/%s" % (pkgdest, pkg)

		lf = bb.utils.lockfile(root + ".lock")

		bb.data.setVar('ROOT', '', localdata)
		bb.data.setVar('ROOT_%s' % pkg, root, localdata)
		pkgname = bb.data.getVar('PKG_%s' % pkg, localdata, 1)
		if not pkgname:
			pkgname = pkg
		bb.data.setVar('PKG', pkgname, localdata)

		bb.data.setVar('OVERRIDES', pkg, localdata)

		bb.data.update_data(localdata)

		conffiles = (bb.data.getVar('CONFFILES', localdata, True) or "").split()

		splitname    = pkgname

		splitsummary = (bb.data.getVar('SUMMARY', d, True) or ".")
		splitversion = (bb.data.getVar('PV', localdata, True) or "").replace('-', '+')
		splitrelease = (bb.data.getVar('PR', localdata, True) or "")
		splitepoch   = (bb.data.getVar('PE', localdata, True) or "")
		splitlicense = (bb.data.getVar('LICENSE', localdata, True) or "")
		splitsection = (bb.data.getVar('SECTION', localdata, True) or "")
		splitdescription = (bb.data.getVar('DESCRIPTION', localdata, True) or "")

		# Roll up the per file dependencies into package level dependencies
		def roll_filerdeps(varname, d):
			depends = bb.utils.explode_dep_versions(bb.data.getVar(varname, d, True) or "")
			dependsflist_key = 'FILE' + varname + 'FLIST'
			dependsflist = (bb.data.getVar(dependsflist_key, d, True) or "")
			for dfile in dependsflist.split():
				key = "FILE" + varname + "_" + dfile
				filedepends = bb.utils.explode_dep_versions(bb.data.getVar(key, d, True) or "")
				bb.utils.extend_deps(depends, filedepends)
			bb.data.setVar(varname, bb.utils.join_deps(depends), d)

		roll_filerdeps('RDEPENDS', localdata)
		roll_filerdeps('RRECOMMENDS', localdata)
		roll_filerdeps('RSUGGESTS', localdata)
		roll_filerdeps('RPROVIDES', localdata)
		roll_filerdeps('RREPLACES', localdata)
		roll_filerdeps('RCONFLICTS', localdata)

		translate_vers('RDEPENDS', localdata)
		translate_vers('RRECOMMENDS', localdata)
		translate_vers('RSUGGESTS', localdata)
		translate_vers('RPROVIDES', localdata)
		translate_vers('RREPLACES', localdata)
		translate_vers('RCONFLICTS', localdata)

		# Map the dependencies into their final form
		bb.build.exec_func("mapping_rename_hook", localdata)

		translate_deps('RDEPENDS', localdata)
		translate_deps('RRECOMMENDS', localdata)
		translate_deps('RSUGGESTS', localdata)
		translate_deps('RPROVIDES', localdata)
		translate_deps('RREPLACES', localdata)
		translate_deps('RCONFLICTS', localdata)

		splitrdepends    = bb.data.getVar('RPMRDEPENDS', localdata, True)
		splitrrecommends = bb.data.getVar('RPMRRECOMMENDS', localdata, True)
		splitrsuggests   = bb.data.getVar('RPMRSUGGESTS', localdata, True)
		splitrprovides   = bb.data.getVar('RPMRPROVIDES', localdata, True)
		splitrreplaces   = bb.data.getVar('RPMRREPLACES', localdata, True)
		splitrconflicts  = bb.data.getVar('RPMRCONFLICTS', localdata, True)
		splitrobsoletes  = []

		# Gather special src/first package data
		if srcname == splitname:
			srcrdepends    = splitrdepends
			srcrrecommends = splitrrecommends
			srcrsuggests   = splitrsuggests
			srcrprovides   = splitrprovides
			srcrreplaces   = splitrreplaces
			srcrconflicts  = splitrconflicts

			srcpreinst  = bb.data.getVar('pkg_preinst', localdata, True)
			srcpostinst = bb.data.getVar('pkg_postinst', localdata, True)
			srcprerm    = bb.data.getVar('pkg_prerm', localdata, True)
			srcpostrm   = bb.data.getVar('pkg_postrm', localdata, True)

			file_list = []
			walk_files(root, file_list, conffiles)
			if not file_list and bb.data.getVar('ALLOW_EMPTY', localdata) != "1":
				bb.note("Not creating empty RPM package for %s" % splitname)
			else:
				bb.note("Creating RPM package for %s" % splitname)
				spec_files_top.append('%files')
				if file_list:
					spec_files_top.extend(file_list)
				spec_files_top.append('')

			bb.utils.unlockfile(lf)
			continue

		# Process subpackage data
		spec_preamble_bottom.append('%%package -n %s' % splitname)
		spec_preamble_bottom.append('Summary: %s' % splitsummary)
		if srcversion != splitversion:
			spec_preamble_bottom.append('Version: %s' % splitversion)
		if srcrelease != splitrelease:
			spec_preamble_bottom.append('Release: %s' % splitrelease)
		if srcepoch != splitepoch:
			spec_preamble_bottom.append('Epoch: %s' % splitepoch)
		if srclicense != splitlicense:
			spec_preamble_bottom.append('License: %s' % splitlicense)
		spec_preamble_bottom.append('Group: %s' % splitsection)

		# Replaces == Obsoletes && Provides
		if splitrreplaces and splitrreplaces.strip() != "":
			for dep in splitrreplaces.split(','):
				if splitrprovides:
					splitrprovides = splitrprovides + ", " + dep
				else:
					splitrprovides = dep
				if splitrobsoletes:
					splitrobsoletes = splitrobsoletes + ", " + dep
				else:
					splitrobsoletes = dep

		if splitrdepends and splitrdepends.strip() != "":
			spec_preamble_bottom.append('Requires: %s' % splitrdepends)
		#if splitrrecommends and splitrrecommends.strip() != "":
		#	spec_preamble_bottom.append('#Recommends: %s' % splitrrecommends)
		#if splitrsuggests and splitrsuggests.strip() != "":
		#	spec_preamble_bottom.append('#Suggests: %s' % splitrsuggests)
		if splitrprovides and splitrprovides.strip() != "":
			spec_preamble_bottom.append('Provides: %s' % splitrprovides)
		if splitrobsoletes and splitrobsoletes.strip() != "":
			spec_preamble_bottom.append('Obsoletes: %s' % splitrobsoletes)
		if splitrconflicts and splitrconflicts.strip() != "":
			for dep in splitrconflicts.split(','):
				# A conflict can NOT be in the provide or an internal conflict happens!
				if dep not in splitrprovides:
					spec_preamble_bottom.append('Conflicts: %s' % dep)

		spec_preamble_bottom.append('')

		spec_preamble_bottom.append('%%description -n %s' % splitname)
		spec_preamble_bottom.append('%s' % splitdescription)

		spec_preamble_bottom.append('')

		# Now process scriptlets
		for script in ["preinst", "postinst", "prerm", "postrm"]:
			scriptvar = bb.data.getVar('pkg_%s' % script, localdata, True)
			if not scriptvar:
				continue
			if script == 'preinst':
				spec_scriptlets_bottom.append('%%pre -n %s' % splitname)
			elif script == 'postinst':
				spec_scriptlets_bottom.append('%%post -n %s' % splitname)
			elif script == 'prerm':
				spec_scriptlets_bottom.append('%%preun -n %s' % splitname)
			elif script == 'postrm':
				spec_scriptlets_bottom.append('%%postun -n %s' % splitname)
			spec_scriptlets_bottom.append(scriptvar)
			spec_scriptlets_bottom.append('')

		# Now process files
		file_list = []
		walk_files(root, file_list, conffiles)
		if not file_list and bb.data.getVar('ALLOW_EMPTY', localdata) != "1":
			bb.note("Not creating empty RPM package for %s" % splitname)
		else:
			bb.note("Creating RPM package for %s" % splitname)
			spec_files_bottom.append('%%files -n %s' % splitname)
			if file_list:
				spec_files_bottom.extend(file_list)
			spec_files_bottom.append('')

		del localdata
		bb.utils.unlockfile(lf)

	spec_preamble_top.append('Summary: %s' % srcsummary)
	spec_preamble_top.append('Name: %s' % srcname)
	spec_preamble_top.append('Version: %s' % srcversion)
	spec_preamble_top.append('Release: %s' % srcrelease)
	if srcepoch and srcepoch.strip() != "":
		spec_preamble_top.append('Epoch: %s' % srcepoch)
	spec_preamble_top.append('License: %s' % srclicense)
	spec_preamble_top.append('Group: %s' % srcsection)
	spec_preamble_top.append('Packager: %s' % srcmaintainer)
	spec_preamble_top.append('URL: %s' % srchomepage)

	# Replaces == Obsoletes && Provides
	if srcrreplaces and srcrreplaces.strip() != "":
		for dep in srcrreplaces.split(','):
			if srcrprovides:
				srcrprovides = srcrprovides + ", " + dep
			else:
				srcrprovides = dep
			if srcrobsoletes:
				srcrobsoletes = srcrobsoletes + ", " + dep
			else:
				srcrobsoletes = dep
	if srcdepends and srcdepends.strip() != "":
		spec_preamble_top.append('BuildRequires: %s' % srcdepends)
	if srcrdepends and srcrdepends.strip() != "":
		spec_preamble_top.append('Requires: %s' % srcrdepends)
	#if srcrrecommends and srcrrecommends.strip() != "":
	#	spec_preamble_top.append('#Recommends: %s' % srcrrecommends)
	#if srcrsuggests and srcrsuggests.strip() != "":
	#	spec_preamble_top.append('#Suggests: %s' % srcrsuggests)
	if srcrprovides and srcrprovides.strip() != "":
		spec_preamble_top.append('Provides: %s' % srcrprovides)
	if srcrobsoletes and srcrobsoletes.strip() != "":
		spec_preamble_top.append('Obsoletes: %s' % srcrobsoletes)
	if srcrconflicts and srcrconflicts.strip() != "":
		for dep in srcrconflicts.split(','):
			# A conflict can NOT be in the provide or an internal conflict happens!
			if dep not in srcrprovides:
				spec_preamble_bottom.append('Conflicts: %s' % dep)

	spec_preamble_top.append('')

	spec_preamble_top.append('%description')
	spec_preamble_top.append('%s' % srcdescription)

	spec_preamble_top.append('')

	if srcpreinst:
		spec_scriptlets_top.append('%pre')
		spec_scriptlets_top.append(srcpreinst)
		spec_scriptlets_top.append('')
	if srcpostinst:
		spec_scriptlets_top.append('%post')
		spec_scriptlets_top.append(srcpostinst)
		spec_scriptlets_top.append('')
	if srcprerm:
		spec_scriptlets_top.append('%preun')
		spec_scriptlets_top.append(srcprerm)
		spec_scriptlets_top.append('')
	if srcpostrm:
		spec_scriptlets_top.append('%postun')
		spec_scriptlets_top.append(srcpostrm)
		spec_scriptlets_top.append('')

	# Write the SPEC file
	try:
		from __builtin__ import file
		specfile = file(outspecfile, 'w')
	except OSError:
		raise bb.build.FuncFailed("unable to open spec file for writing.")

	for line in spec_preamble_top:
		specfile.write(line + "\n")

	for line in spec_preamble_bottom:
		specfile.write(line + "\n")

	for line in spec_scriptlets_top:
		specfile.write(line + "\n")

	for line in spec_scriptlets_bottom:
		specfile.write(line + "\n")

	for line in spec_files_top:
		specfile.write(line + "\n")

	for line in spec_files_bottom:
		specfile.write(line + "\n")

	specfile.close()
}

python do_package_rpm () {
	import os

	workdir = bb.data.getVar('WORKDIR', d, True)
	outdir = bb.data.getVar('DEPLOY_DIR_IPK', d, True)
	dvar = bb.data.getVar('D', d, True)
	tmpdir = bb.data.getVar('TMPDIR', d, True)
	pkgd = bb.data.getVar('PKGD', d, True)
	pkgdest = bb.data.getVar('PKGDEST', d, True)
	if not workdir or not outdir or not dvar or not tmpdir:
		bb.error("Variables incorrectly set, unable to package")
		return

	if not os.path.exists(dvar):
		bb.debug(1, "Nothing installed, nothing to do")
		return

	packages = bb.data.getVar('PACKAGES', d, True)
	if not packages or packages == '':
		bb.debug(1, "No packages; nothing to do")
		return

	# Construct the spec file...
	srcname    = bb.data.getVar('PN', d, True)
	outspecfile = workdir + "/" + srcname + ".spec"
	bb.data.setVar('OUTSPECFILE', outspecfile, d)
	bb.build.exec_func('write_specfile', d)

	# Setup the rpmbuild arguments...
	rpmbuild = bb.data.getVar('RPMBUILD', d, True)
	targetsys = bb.data.getVar('TARGET_SYS', d, True)
	pkgwritedir = bb.data.expand('${PKGWRITEDIRRPM}/${PACKAGE_ARCH}', d)
	pkgarch = bb.data.expand('${PACKAGE_ARCH}', d)
	bb.mkdirhier(pkgwritedir)
	os.chmod(pkgwritedir, 0755)

	cmd = rpmbuild
	cmd = cmd + " --nodeps --short-circuit --target " + pkgarch + " --buildroot " + pkgd
	cmd = cmd + " --define '_topdir " + workdir + "' --define '_rpmdir " + pkgwritedir + "'"
	cmd = cmd + " --define '_build_name_fmt %%{NAME}-%%{VERSION}-%%{RELEASE}.%%{ARCH}.rpm'"
	cmd = cmd + " -bb " + outspecfile

	# Build the spec file!
	bb.data.setVar('BUILDSPEC', cmd + "\n", d)
	bb.data.setVarFlag('BUILDSPEC', 'func', '1', d)
	bb.build.exec_func('BUILDSPEC', d)
}

python () {
    if bb.data.getVar('PACKAGES', d, True) != '':
        deps = (bb.data.getVarFlag('do_package_write_rpm', 'depends', d) or "").split()
        deps.append('rpm-native:do_populate_sysroot')
        deps.append('virtual/fakeroot-native:do_populate_sysroot')
        bb.data.setVarFlag('do_package_write_rpm', 'depends', " ".join(deps), d)
        bb.data.setVarFlag('do_package_write_rpm', 'fakeroot', 1, d)
}

SSTATETASKS += "do_package_write_rpm"
do_package_write_rpm[sstate-name] = "deploy-rpm"
do_package_write_rpm[sstate-inputdirs] = "${PKGWRITEDIRRPM}"
do_package_write_rpm[sstate-outputdirs] = "${DEPLOY_DIR_RPM}"

python do_package_write_rpm_setscene () {
	sstate_setscene(d)
}
#addtask do_package_write_rpm_setscene

python do_package_write_rpm () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("do_package_rpm", d)
}

do_package_write_rpm[dirs] = "${PKGWRITEDIRRPM}"
addtask package_write_rpm before do_package_write after do_package

