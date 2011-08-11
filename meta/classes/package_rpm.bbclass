inherit package

IMAGE_PKGTYPE ?= "rpm"

RPM="rpm"
RPMBUILD="rpmbuild"

PKGWRITEDIRRPM = "${WORKDIR}/deploy-rpms"

python package_rpm_fn () {
	bb.data.setVar('PKGFN', bb.data.getVar('PKG',d), d)
}

python package_rpm_install () {
	bb.fatal("package_rpm_install not implemented!")
}

RPMCONF_TARGET_BASE = "${DEPLOY_DIR_RPM}/solvedb"
RPMCONF_HOST_BASE = "${DEPLOY_DIR_RPM}/solvedb-sdk"
#
# Update the Packages depsolver db in ${DEPLOY_DIR_RPM}
#
package_update_index_rpm () {
	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" ]; then
		return
	fi

	# Update target packages
	base_archs="${PACKAGE_ARCHS}"
	ml_archs="${MULTILIB_PACKAGE_ARCHS}"
	package_update_index_rpm_common "${RPMCONF_TARGET_BASE}" base_archs ml_archs

	# Update SDK packages
	base_archs="${SDK_PACKAGE_ARCHS}"
	package_update_index_rpm_common "${RPMCONF_HOST_BASE}" base_archs
}

package_update_index_rpm_common () {
	rpmconf_base="$1"
	shift

	for archvar in "$@"; do
		eval archs=\${${archvar}}
		packagedirs=""
		for arch in $archs; do
			packagedirs="${DEPLOY_DIR_RPM}/$arch $packagedirs"
			rm -rf ${DEPLOY_DIR_RPM}/$arch/solvedb
		done

		cat /dev/null > ${rpmconf_base}-${archvar}.conf
		for pkgdir in $packagedirs; do
			if [ -e $pkgdir/ ]; then
				echo "Generating solve db for $pkgdir..."
				echo $pkgdir/solvedb >> ${rpmconf_base}-${archvar}.conf
				if [ -d $pkgdir/solvedb ]; then
					# We've already processed this and it's a duplicate
					continue
				fi
				mkdir -p $pkgdir/solvedb
				echo "# Dynamically generated solve manifest" >> $pkgdir/solvedb/manifest
				find $pkgdir -maxdepth 1 -type f >> $pkgdir/solvedb/manifest
				${RPM} -i --replacepkgs --replacefiles --oldpackage \
					-D "_dbpath $pkgdir/solvedb" --justdb \
					--noaid --nodeps --noorder --noscripts --notriggers --noparentdirs --nolinktos --stats \
					--ignoresize --nosignature --nodigest \
					-D "__dbi_txn create nofsync" \
					$pkgdir/solvedb/manifest
			fi
		done
	done
}

#
# Generate an rpm configuration suitable for use against the
# generated depsolver db's...
#
package_generate_rpm_conf () {
	# Update target packages
	package_generate_rpm_conf_common "${RPMCONF_TARGET_BASE}" base_archs ml_archs

	# Update SDK packages
	package_generate_rpm_conf_common "${RPMCONF_HOST_BASE}" base_archs
}

package_generate_rpm_conf_common() {
	rpmconf_base="$1"
	shift

	printf "_solve_dbpath " > ${rpmconf_base}.macro
	o_colon=false

	for archvar in "$@"; do
		printf "_solve_dbpath " > ${rpmconf_base}-${archvar}.macro
		colon=false
		for each in `cat ${rpmconf_base}-${archvar}.conf` ; do
			if [ "$o_colon" == true ]; then
				printf ":" >> ${rpmconf_base}.macro
			fi
			if [ "$colon" == true ]; then
				printf ":" >> ${rpmconf_base}-${archvar}.macro
			fi
			printf "%s" $each >> ${rpmconf_base}.macro
			o_colon=true
			printf "%s" $each >> ${rpmconf_base}-${archvar}.macro
			colon=true
		done
		printf "\n" >> ${rpmconf_base}-${archvar}.macro
	done
	printf "\n" >> ${rpmconf_base}.macro
}

rpm_log_check() {
       target="$1"
       lf_path="$2"

       lf_txt="`cat $lf_path`"
       for keyword_die in "Cannot find package" "exit 1" ERR Fail
       do
               if (echo "$lf_txt" | grep -v log_check | grep "$keyword_die") >/dev/null 2>&1
               then
                       echo "log_check: There were error messages in the logfile"
                       echo -e "log_check: Matched keyword: [$keyword_die]\n"
                       echo "$lf_txt" | grep -v log_check | grep -C 5 -i "$keyword_die"
                       echo ""
                       do_exit=1
               fi
       done
       test "$do_exit" = 1 && exit 1
       true
}


#
# Resolve package names to filepaths
# resolve_pacakge <pkgname> <solvdb conffile>
#
resolve_package_rpm () {
	local conffile="$1"
	shift
	local pkg_name=""
	for solve in `cat ${conffile}`; do
		pkg_name=$(${RPM} -D "_dbpath $solve" -D "__dbi_txn create nofsync" -q --yaml $@ | grep -i 'Packageorigin' | cut -d : -f 2)
		if [ -n "$pkg_name" ]; then
			break;
		fi
	done
	echo $pkg_name
}

#
# install a bunch of packages using rpm
# the following shell variables needs to be set before calling this func:
# INSTALL_ROOTFS_RPM - install root dir
# INSTALL_PLATFORM_RPM - main platform
# INSTALL_PLATFORM_EXTRA_RPM - extra platform
# INSTALL_CONFBASE_RPM - configuration file base name
# INSTALL_PACKAGES_NORMAL_RPM - packages to be installed
# INSTALL_PACKAGES_ATTEMPTONLY_RPM - packages attemped to be installed only
# INSTALL_PACKAGES_LINGUAS_RPM - additional packages for uclibc
# INSTALL_PROVIDENAME_RPM - content for provide name
# INSTALL_TASK_RPM - task name

package_install_internal_rpm () {

	local target_rootfs="${INSTALL_ROOTFS_RPM}"
	local platform="${INSTALL_PLATFORM_RPM}"
	local platform_extra="${INSTALL_PLATFORM_EXTRA_RPM}"
	local confbase="${INSTALL_CONFBASE_RPM}"
	local package_to_install="${INSTALL_PACKAGES_NORMAL_RPM}"
	local package_attemptonly="${INSTALL_PACKAGES_ATTEMPTONLY_RPM}"
	local package_linguas="${INSTALL_PACKAGES_LINGUAS_RPM}"
	local providename="${INSTALL_PROVIDENAME_RPM}"
	local task="${INSTALL_TASK_RPM}"

	# Setup base system configuration
	mkdir -p ${target_rootfs}/etc/rpm/
	echo "${platform}${TARGET_VENDOR}-${TARGET_OS}" > ${target_rootfs}/etc/rpm/platform
	if [ ! -z "$platform_extra" ]; then
		for pt in $platform_extra ; do
			case $pt in
				noarch | any | all)
					os="`echo ${TARGET_OS} | sed "s,-.*,,"`.*"
					;;
				*)
					os="${TARGET_OS}"
					;;
			esac
			echo "$pt-.*-$os" >> ${target_rootfs}/etc/rpm/platform
		done
	fi

	# Tell RPM that the "/" directory exist and is available
	mkdir -p ${target_rootfs}/etc/rpm/sysinfo
	echo "/" >${target_rootfs}/etc/rpm/sysinfo/Dirnames
	if [ ! -z "$providename" ]; then
		cat /dev/null > ${target_rootfs}/etc/rpm/sysinfo/Providename
		for provide in $providename ; do
			echo $provide >> ${target_rootfs}/etc/rpm/sysinfo/Providename
		done
	fi

	# Setup manifest of packages to install...
	mkdir -p ${target_rootfs}/install
	echo "# Install manifest" > ${target_rootfs}/install/install.manifest

	# Uclibc builds don't provide this stuff...
	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then
		if [ ! -z "${package_linguas}" ]; then
			for pkg in ${package_linguas}; do
				echo "Processing $pkg..."

				archvar=base_archs
				ml_pkg=$(echo ${pkg} | sed "s,^${MLPREFIX}\(.*\),\1,")
				if [ "${ml_pkg}" != "${pkg}" ]; then
					archvar=ml_archs
				fi

				pkg_name=$(resolve_package_rpm ${confbase}-${archvar}.conf ${ml_pkg})
				if [ -z "$pkg_name" ]; then
					echo "Unable to find package $pkg ($ml_pkg)!"
					exit 1
				fi
				echo $pkg_name >> ${target_rootfs}/install/install.manifest
			done
		fi
	fi

	if [ ! -z "${package_to_install}" ]; then
		for pkg in ${package_to_install} ; do
			echo "Processing $pkg..."

			archvar=base_archs
			ml_pkg=$(echo ${pkg} | sed "s,^${MLPREFIX}\(.*\),\1,")
			if [ "${ml_pkg}" != "${pkg}" ]; then
				archvar=ml_archs
			fi

			pkg_name=$(resolve_package_rpm ${confbase}-${archvar}.conf ${ml_pkg})
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg ($ml_pkg)!"
				exit 1
			fi
			echo $pkg_name >> ${target_rootfs}/install/install.manifest
		done
	fi

	# Generate an install solution by doing a --justdb install, then recreate it with
	# an actual package install!
	${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
		--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
		-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}.macro`" \
		-D "__dbi_txn create nofsync" \
		-U --justdb --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
		${target_rootfs}/install/install.manifest

	if [ ! -z "${package_attemptonly}" ]; then
		echo "Adding attempt only packages..."
		for pkg in ${package_attemptonly} ; do
			echo "Processing $pkg..."
			pkg_name=$(resolve_package_rpm $pkg ${confbase}.conf)
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg!"
				exit 1
			fi
			echo "Attempting $pkg_name..." >> "${WORKDIR}/temp/log.do_${task}_attemptonly.${PID}"
			${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
				--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
				-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}.macro`" \
				-D "__dbi_txn create nofsync private" \
				-U --justdb --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
			$pkg_name >> "${WORKDIR}/temp/log.do_${task}_attemptonly.${PID}" || true
		done
	fi

	#### Note: 'Recommends' is an arbitrary tag that means _SUGGESTS_ in Poky..
	# Add any recommended packages to the image
	# RPM does not solve for recommended packages because they are optional...
	# So we query them and tree them like the ATTEMPTONLY packages above...
	# Change the loop to "1" to run this code...
	loop=0
	if [ $loop -eq 1 ]; then
	 echo "Processing recommended packages..."
	 cat /dev/null >  ${target_rootfs}/install/recommend.list
	 while [ $loop -eq 1 ]; do
		# Dump the full set of recommends...
		${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
			--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
			-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}.macro`" \
			-D "__dbi_txn create nofsync private" \
			-qa --qf "[%{RECOMMENDS}\n]" | sort -u > ${target_rootfs}/install/recommend
		# Did we add more to the list?
		grep -v -x -F -f ${target_rootfs}/install/recommend.list ${target_rootfs}/install/recommend > ${target_rootfs}/install/recommend.new || true
		# We don't want to loop unless there is a change to the list!
		loop=0
		cat ${target_rootfs}/install/recommend.new | \
		 while read pkg ; do
			# Ohh there was a new one, we'll need to loop again...
			loop=1
			echo "Processing $pkg..."
			pkg_name=$(resolve_package $pkg ${confbase}.conf)
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg." >> "${WORKDIR}/temp/log.do_${task}_recommend.${PID}"
				continue
			fi
			echo "Attempting $pkg_name..." >> "${WORKDIR}/temp/log.do_{task}_recommend.${PID}"
			${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
				--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
				-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}.macro`" \
				-D "__dbi_txn create nofsync private" \
				-U --justdb --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
				$pkg_name >> "${WORKDIR}/temp/log.do_${task}_recommend.${PID}" 2>&1 || true
		done
		cat ${target_rootfs}/install/recommend.list ${target_rootfs}/install/recommend.new | sort -u > ${target_rootfs}/install/recommend.new.list
		mv -f ${target_rootfs}/install/recommend.new.list ${target_rootfs}/install/recommend.list
		rm ${target_rootfs}/install/recommend ${target_rootfs}/install/recommend.new
	 done
	fi

	# Now that we have a solution, pull out a list of what to install...
	echo "Manifest: ${target_rootfs}/install/install.manifest"
	${RPM} -D "_dbpath ${target_rootfs}/install" -qa --yaml \
		-D "__dbi_txn create nofsync private" \
		| grep -i 'Packageorigin' | cut -d : -f 2 > ${target_rootfs}/install/install_solution.manifest

	# Attempt install
	${RPM} --root ${target_rootfs} \
		--predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
		--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
		-D "_dbpath ${rpmlibdir}" \
		--noscripts --notriggers --noparentdirs --nolinktos \
		-D "__dbi_txn create nofsync private" \
		-Uhv ${target_rootfs}/install/install_solution.manifest
}

python write_specfile () {
	import textwrap
	import oe.packagedata

	# We need a simple way to remove the MLPREFIX from the package name,
	# and dependency information...
	def strip_multilib(name, d):
		multilibs = d.getVar('MULTILIBS', True) or ""
		for ext in multilibs.split():
			eext = ext.split(':')
			if len(eext) > 1 and eext[0] == 'multilib' and name and name.find(eext[1] + '-') == 0:
				name = (eext[1] + '-').join(name.split(eext[1] + '-', 1)[1:])
		return name

#		ml = bb.data.getVar("MLPREFIX", d, True)
#		if ml and name and len(ml) != 0 and name.find(ml) == 0:
#			return ml.join(name.split(ml, 1)[1:])
#		return name

	# In RPM, dependencies are of the format: pkg <>= Epoch:Version-Release
	# This format is similar to OE, however there are restrictions on the
	# characters that can be in a field.  In the Version field, "-"
	# characters are not allowed.  "-" is allowed in the Release field.
	#
	# We translate the "-" in the version to a "+", by loading the PKGV
	# from the dependent recipe, replacing the - with a +, and then using
	# that value to do a replace inside of this recipe's dependencies.
	# This preserves the "-" separator between the version and release, as
	# well as any "-" characters inside of the release field.
	#
	# All of this has to happen BEFORE the mapping_rename_hook as
	# after renaming we cannot look up the dependencies in the packagedata
	# store.
	def translate_vers(varname, d):
		depends = bb.data.getVar(varname, d, True)
		if depends:
			depends_dict = bb.utils.explode_dep_versions(depends)
			newdeps_dict = {}
			for dep in depends_dict:
				ver = depends_dict[dep]
				if dep and ver:
					if '-' in ver:
						subd = oe.packagedata.read_subpkgdata_dict(dep, d)
						if 'PKGV' in subd:
							pv = subd['PKGV']
							reppv = pv.replace('-', '+')
							ver = ver.replace(pv, reppv)
				newdeps_dict[strip_multilib(dep, d)] = ver
			depends = bb.utils.join_deps(newdeps_dict)
			bb.data.setVar(varname, depends.strip(), d)

	# We need to change the style the dependency from BB to RPM
	# This needs to happen AFTER the mapping_rename_hook
	def print_deps(variable, tag, array, d):
		depends = variable
		if depends:
			depends_dict = bb.utils.explode_dep_versions(depends)
			for dep in depends_dict:
				ver = depends_dict[dep]
				if dep and ver:
					ver = ver.replace('(', '')
					ver = ver.replace(')', '')
					array.append("%s: %s %s" % (tag, dep, ver))
				else:
					array.append("%s: %s" % (tag, dep))

	def walk_files(walkpath, target, conffiles):
		import os
		for rootpath, dirs, files in os.walk(walkpath):
			path = rootpath.replace(walkpath, "")
			for dir in dirs:
				# All packages own the directories their files are in...
				target.append("%dir " + path + "/" + dir)
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
	srcname    = strip_multilib(bb.data.getVar('PN', d, True), d)
	srcsummary = (bb.data.getVar('SUMMARY', d, True) or bb.data.getVar('DESCRIPTION', d, True) or ".")
	srcversion = bb.data.getVar('PKGV', d, True).replace('-', '+')
	srcrelease = bb.data.getVar('PKGR', d, True)
	srcepoch   = (bb.data.getVar('PKGE', d, True) or "")
	srclicense = bb.data.getVar('LICENSE', d, True)
	srcsection = bb.data.getVar('SECTION', d, True)
	srcmaintainer  = bb.data.getVar('MAINTAINER', d, True)
	srchomepage    = bb.data.getVar('HOMEPAGE', d, True)
	srcdescription = bb.data.getVar('DESCRIPTION', d, True) or "."

	srcdepends     = strip_multilib(bb.data.getVar('DEPENDS', d, True), d)
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

		splitname    = strip_multilib(pkgname, d)

		splitsummary = (bb.data.getVar('SUMMARY', localdata, True) or bb.data.getVar('DESCRIPTION', localdata, True) or ".")
		splitversion = (bb.data.getVar('PKGV', localdata, True) or "").replace('-', '+')
		splitrelease = (bb.data.getVar('PKGR', localdata, True) or "")
		splitepoch   = (bb.data.getVar('PKGE', localdata, True) or "")
		splitlicense = (bb.data.getVar('LICENSE', localdata, True) or "")
		splitsection = (bb.data.getVar('SECTION', localdata, True) or "")
		splitdescription = (bb.data.getVar('DESCRIPTION', localdata, True) or ".")

		translate_vers('RDEPENDS', localdata)
		translate_vers('RRECOMMENDS', localdata)
		translate_vers('RSUGGESTS', localdata)
		translate_vers('RPROVIDES', localdata)
		translate_vers('RREPLACES', localdata)
		translate_vers('RCONFLICTS', localdata)

		# Map the dependencies into their final form
		bb.build.exec_func("mapping_rename_hook", localdata)

		splitrdepends    = bb.data.getVar('RDEPENDS', localdata, True) or ""
		splitrrecommends = bb.data.getVar('RRECOMMENDS', localdata, True) or ""
		splitrsuggests   = bb.data.getVar('RSUGGESTS', localdata, True) or ""
		splitrprovides   = bb.data.getVar('RPROVIDES', localdata, True) or ""
		splitrreplaces   = bb.data.getVar('RREPLACES', localdata, True) or ""
		splitrconflicts  = bb.data.getVar('RCONFLICTS', localdata, True) or ""
		splitrobsoletes  = []

		# For now we need to manually supplement RPROVIDES with any update-alternatives links
		if pkg == d.getVar("PN", True):
			splitrprovides = splitrprovides + " " + (d.getVar('ALTERNATIVE_LINK', True) or '') + " " + (d.getVar('ALTERNATIVE_LINKS', True) or '')

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
				spec_files_top.append('%defattr(-,-,-,-)')
				if file_list:
					bb.note("Creating RPM package for %s" % splitname)
					spec_files_top.extend(file_list)
				else:
					bb.note("Creating EMPTY RPM Package for %s" % splitname)
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

		print_deps(splitrdepends,	"Requires", spec_preamble_bottom, d)
		# Suggests in RPM are like recommends in Poky!
		print_deps(splitrrecommends,	"Suggests", spec_preamble_bottom, d)
		# While there is no analog for suggests... (So call them recommends for now)
		print_deps(splitrsuggests, 	"Recommends", spec_preamble_bottom, d)
		print_deps(splitrprovides, 	"Provides", spec_preamble_bottom, d)
		print_deps(splitrobsoletes, 	"Obsoletes", spec_preamble_bottom, d)

		# conflicts can not be in a provide!  We will need to filter it.
		if splitrconflicts:
			depends_dict = bb.utils.explode_dep_versions(splitrconflicts)
			newdeps_dict = {}
			for dep in depends_dict:
				if dep not in splitrprovides:
					newdeps_dict[dep] = depends_dict[dep]
			if newdeps_dict:
				splitrconflicts = bb.utils.join_deps(newdeps_dict)
			else:
				splitrconflicts = ""

		print_deps(splitrconflicts, 	"Conflicts", spec_preamble_bottom, d)

		spec_preamble_bottom.append('')

		spec_preamble_bottom.append('%%description -n %s' % splitname)
		dedent_text = textwrap.dedent(splitdescription).strip()
		spec_preamble_bottom.append('%s' % textwrap.fill(dedent_text, width=75))

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
			spec_files_bottom.append('%%files -n %s' % splitname)
			spec_files_bottom.append('%defattr(-,-,-,-)')
			if file_list:
				bb.note("Creating RPM package for %s" % splitname)
				spec_files_bottom.extend(file_list)
			else:
				bb.note("Creating EMPTY RPM Package for %s" % splitname)
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

	print_deps(srcdepends,		"BuildRequires", spec_preamble_top, d)
	print_deps(srcrdepends,		"Requires", spec_preamble_top, d)
	# Suggests in RPM are like recommends in Poky!
	print_deps(srcrrecommends,	"Suggests", spec_preamble_top, d)
	# While there is no analog for suggests... (So call them recommends for now)
	print_deps(srcrsuggests, 	"Recommends", spec_preamble_top, d)
	print_deps(srcrprovides, 	"Provides", spec_preamble_top, d)
	print_deps(srcrobsoletes, 	"Obsoletes", spec_preamble_top, d)

	# conflicts can not be in a provide!  We will need to filter it.
	if srcrconflicts:
		depends_dict = bb.utils.explode_dep_versions(srcrconflicts)
		newdeps_dict = {}
		for dep in depends_dict:
			if dep not in srcrprovides:
				newdeps_dict[dep] = depends_dict[dep]
		if newdeps_dict:
			srcrconflicts = bb.utils.join_deps(newdeps_dict)
		else:
			srcrconflicts = ""

	print_deps(srcrconflicts, 	"Conflicts", spec_preamble_top, d)

	spec_preamble_top.append('')

	spec_preamble_top.append('%description')
	dedent_text = textwrap.dedent(srcdescription).strip()
	spec_preamble_top.append('%s' % textwrap.fill(dedent_text, width=75))

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

	# We need a simple way to remove the MLPREFIX from the package name,
	# and dependency information...
	def strip_multilib(name, d):
		ml = bb.data.getVar("MLPREFIX", d, True)
		if ml and name and len(ml) != 0 and name.find(ml) == 0:
			return ml.join(name.split(ml, 1)[1:])
		return name

	workdir = bb.data.getVar('WORKDIR', d, True)
	outdir = bb.data.getVar('DEPLOY_DIR_IPK', d, True)
	tmpdir = bb.data.getVar('TMPDIR', d, True)
	pkgd = bb.data.getVar('PKGD', d, True)
	pkgdest = bb.data.getVar('PKGDEST', d, True)
	if not workdir or not outdir or not pkgd or not tmpdir:
		bb.error("Variables incorrectly set, unable to package")
		return

	packages = bb.data.getVar('PACKAGES', d, True)
	if not packages or packages == '':
		bb.debug(1, "No packages; nothing to do")
		return

	# Construct the spec file...
	srcname    = strip_multilib(bb.data.getVar('PN', d, True), d)
	outspecfile = workdir + "/" + srcname + ".spec"
	bb.data.setVar('OUTSPECFILE', outspecfile, d)
	bb.build.exec_func('write_specfile', d)

	# Construct per file dependencies file
	def dump_filerdeps(varname, outfile, d):
		outfile.write("#!/bin/sh\n")
		outfile.write("\n# Dependency table\n")
		for pkg in packages.split():
			dependsflist_key = 'FILE' + varname + 'FLIST' + "_" + pkg
			dependsflist = (bb.data.getVar(dependsflist_key, d, True) or "")
			for dfile in dependsflist.split():
				key = "FILE" + varname + "_" + dfile + "_" + pkg
				depends_dict = bb.utils.explode_dep_versions(bb.data.getVar(key, d, True) or "")
				file = dfile.replace("@underscore@", "_")
				file = file.replace("@closebrace@", "]")
				file = file.replace("@openbrace@", "[")
				file = file.replace("@tab@", "\t")
				file = file.replace("@space@", " ")
				file = file.replace("@at@", "@")
				outfile.write("#" + pkgd + file + "\t")
				for dep in depends_dict:
					ver = depends_dict[dep]
					if dep and ver:
						ver = ver.replace("(","")
						ver = ver.replace(")","")
						outfile.write(dep + " " + ver + " ")
					else:
						outfile.write(dep + " ")
				outfile.write("\n")
		outfile.write("\n\nwhile read file_name ; do\n")
		outfile.write("\tlength=$(echo \"#${file_name}\t\" | wc -c )\n")
		outfile.write("\tline=$(grep \"^#${file_name}\t\" $0 | cut -c ${length}- )\n")
		outfile.write("\tprintf \"%s\\n\" ${line}\n")
		outfile.write("done\n")

	# Poky dependencies a.k.a. RPM requires
	outdepends = workdir + "/" + srcname + ".requires"

	try:
		from __builtin__ import file
		dependsfile = file(outdepends, 'w')
	except OSError:
		raise bb.build.FuncFailed("unable to open spec file for writing.")

	dump_filerdeps('RDEPENDS', dependsfile, d)

	dependsfile.close()
	os.chmod(outdepends, 0755)

	# Poky / RPM Provides
	outprovides = workdir + "/" + srcname + ".requires"

	try:
		from __builtin__ import file
		providesfile = file(outprovides, 'w')
	except OSError:
		raise bb.build.FuncFailed("unable to open spec file for writing.")

	dump_filerdeps('RPROVIDES', providesfile, d)

	providesfile.close()
	os.chmod(outprovides, 0755)

	# Setup the rpmbuild arguments...
	rpmbuild = bb.data.getVar('RPMBUILD', d, True)
	targetsys = bb.data.getVar('TARGET_SYS', d, True)
	targetvendor = bb.data.getVar('TARGET_VENDOR', d, True)
	pkgwritedir = bb.data.expand('${PKGWRITEDIRRPM}/${PACKAGE_ARCH}', d)
	pkgarch = bb.data.expand('${PACKAGE_ARCH}${TARGET_VENDOR}-${TARGET_OS}', d)
	magicfile = bb.data.expand('${STAGING_DIR_NATIVE}/usr/share/misc/magic.mgc', d)
	bb.mkdirhier(pkgwritedir)
	os.chmod(pkgwritedir, 0755)

	cmd = rpmbuild
	cmd = cmd + " --nodeps --short-circuit --target " + pkgarch + " --buildroot " + pkgd
	cmd = cmd + " --define '_topdir " + workdir + "' --define '_rpmdir " + pkgwritedir + "'"
	cmd = cmd + " --define '_build_name_fmt %%{NAME}-%%{VERSION}-%%{RELEASE}.%%{ARCH}.rpm'"
	cmd = cmd + " --define '_use_internal_dependency_generator 0'"
	cmd = cmd + " --define '__find_requires " + outdepends + "'"
	cmd = cmd + " --define '__find_provides " + outprovides + "'"
	cmd = cmd + " --define '_unpackaged_files_terminate_build 0'"
	cmd = cmd + " --define 'debug_package %{nil}'"
	cmd = cmd + " --define '_rpmfc_magic_path " + magicfile + "'"
	cmd = cmd + " -bb " + outspecfile

	# Build the rpm package!
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
        bb.data.setVarFlag('do_package_write_rpm_setscene', 'fakeroot', 1, d)
}

SSTATETASKS += "do_package_write_rpm"
do_package_write_rpm[sstate-name] = "deploy-rpm"
do_package_write_rpm[sstate-inputdirs] = "${PKGWRITEDIRRPM}"
do_package_write_rpm[sstate-outputdirs] = "${DEPLOY_DIR_RPM}"
# Take a shared lock, we can write multiple packages at the same time...
# but we need to stop the rootfs/solver from running while we do...
do_package_write_rpm[sstate-lockfile-shared] += "${DEPLOY_DIR_RPM}/rpm.lock"

python do_package_write_rpm_setscene () {
	sstate_setscene(d)
}
addtask do_package_write_rpm_setscene

python do_package_write_rpm () {
	bb.build.exec_func("read_subpackage_metadata", d)
	bb.build.exec_func("do_package_rpm", d)
}

do_package_write_rpm[dirs] = "${PKGWRITEDIRRPM}"
addtask package_write_rpm before do_package_write after do_package

PACKAGEINDEXES += "package_update_index_rpm; createrepo ${DEPLOY_DIR_RPM};"
PACKAGEINDEXDEPS += "rpm-native:do_populate_sysroot"
PACKAGEINDEXDEPS += "createrepo-native:do_populate_sysroot"
