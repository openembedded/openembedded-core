inherit package

IMAGE_PKGTYPE ?= "rpm"

RPM="rpm"
RPMBUILD="rpmbuild"

PKGWRITEDIRRPM = "${WORKDIR}/deploy-rpms"
PKGWRITEDIRSRPM = "${DEPLOY_DIR}/sources/deploy-srpm"

python package_rpm_fn () {
    d.setVar('PKGFN', d.getVar('PKG'))
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
	if [ ! -z "${DEPLOY_KEEP_PACKAGES}" -o ! -e "${DEPLOY_DIR_RPM}" ]; then
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

        createdirs=""
	for archvar in "$@"; do
		eval archs=\${${archvar}}
		packagedirs=""
		for arch in $archs; do
			packagedirs="${DEPLOY_DIR_RPM}/$arch $packagedirs"
			rm -rf ${DEPLOY_DIR_RPM}/$arch/solvedb.done
		done

		cat /dev/null > ${rpmconf_base}-${archvar}.conf
		for pkgdir in $packagedirs; do
			if [ -e $pkgdir/ ]; then
				echo "Generating solve db for $pkgdir..."
				echo $pkgdir/solvedb >> ${rpmconf_base}-${archvar}.conf
                                createdirs="$createdirs $pkgdir"
			fi
		done
	done
	rpm-createsolvedb.py "${RPM}" $createdirs
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
	o_colon="false"

	for archvar in "$@"; do
		printf "_solve_dbpath " > ${rpmconf_base}-${archvar}.macro
		colon="false"
		for each in `cat ${rpmconf_base}-${archvar}.conf` ; do
			if [ "$o_colon" = "true" ]; then
				printf ":" >> ${rpmconf_base}.macro
			fi
			if [ "$colon" = "true" ]; then
				printf ":" >> ${rpmconf_base}-${archvar}.macro
			fi
			printf "%s" $each >> ${rpmconf_base}.macro
			o_colon="true"
			printf "%s" $each >> ${rpmconf_base}-${archvar}.macro
			colon="true"
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
		pkg_name=$(${RPM} -D "_dbpath $solve" -D "__dbi_txn create nofsync" -q --qf "%{packageorigin}\n" "$@" | grep -v "is not installed" || true)
		if [ -n "$pkg_name" -a "$pkg_name" != "(none)" ]; then
			echo $pkg_name
			break;
		fi
	done
}

# rpm common command and options
rpm_common_comand () {

    local target_rootfs="${INSTALL_ROOTFS_RPM}"

    ${RPM} --root ${target_rootfs} \
        --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
        --predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
        -D "_var ${localstatedir}" \
        -D "_dbpath ${rpmlibdir}" \
        -D "_tmppath /install/tmp" \
        --noparentdirs --nolinktos \
        -D "__dbi_txn create nofsync private" \
        -D "_cross_scriptlet_wrapper ${WORKDIR}/scriptlet_wrapper" $@
}

# install or remove the pkg
rpm_update_pkg () {

    manifest=$1
    # The manifest filename, e.g. total_solution.manifest
    m_name=${manifest##/*/}
    local target_rootfs="${INSTALL_ROOTFS_RPM}"
    installdir=$target_rootfs/install
    pre_btmanifest=$installdir/pre_bt.manifest
    cur_btmanifest=$installdir/cur_bt.manifest

    # Install/remove the different pkgs when total_solution.manifest is
    # comming and incremental image generation is enabled.
    if [ "${INC_RPM_IMAGE_GEN}" = "1" -a -d "${target_rootfs}${rpmlibdir}" \
        -a "$m_name" = "total_solution.manifest" \
        -a "${INSTALL_COMPLEMENTARY_RPM}" != "1" ]; then
        # Get the previous installed list
        rpm --root $target_rootfs --dbpath ${rpmlibdir} \
            -qa --qf '%{PACKAGEORIGIN} %{BUILDTIME}\n' | sort -u -o $pre_btmanifest
        # Get the current installed list (based on install/var/lib/rpm)
        rpm --root $installdir -D "_dbpath $installdir" \
            -qa --qf '%{PACKAGEORIGIN} %{BUILDTIME}\n' | sort -u -o $cur_btmanifest
        comm -1 -3 $cur_btmanifest $pre_btmanifest | sed 's#.*/\(.*\)\.rpm .*#\1#' > \
            $installdir/remove.manifest
        comm -2 -3 $cur_btmanifest $pre_btmanifest | awk '{print $1}' > \
            $installdir/incremental.manifest

        # Attempt to remove unwanted pkgs, the scripts(pre, post, etc.) has not
        # been run by now, so don't have to run them(preun, postun, etc.) when
        # erase the pkg
        if [ -s $installdir/remove.manifest ]; then
            rpm_common_comand --noscripts --nodeps \
                -e `cat $installdir/remove.manifest`
        fi

        # Attempt to install the incremental pkgs
        if [ -s $installdir/incremental.manifest ]; then
            rpm_common_comand --nodeps --replacefiles --replacepkgs \
               -Uvh $installdir/incremental.manifest
        fi
    else
        # Attempt to install
        rpm_common_comand --replacepkgs -Uhv $manifest
    fi
}

process_pkg_list_rpm() {
	local insttype=$1
	shift
	# $@ is special POSIX linear array can not be assigned
	# to a local variable directly in dash since its separated by
	# space and dash expands it before assignment
	# and local x=1 2 3 and not x="1 2 3"
	local pkgs
	pkgs="$@"
	local confbase=${INSTALL_CONFBASE_RPM}

	echo -n > ${target_rootfs}/install/base_archs.pkglist
	echo -n > ${target_rootfs}/install/ml_archs.pkglist

	for pkg in $pkgs; do
		echo "Processing $pkg..."

		archvar=base_archs
		ml_pkg=$pkg
		for i in ${MULTILIB_PREFIX_LIST} ; do
				subst=${pkg#${i}-}
				if [ $subst != $pkg ] ; then
						ml_pkg=$subst
						archvar=ml_archs
						break
				fi
		done

		echo $ml_pkg >> ${target_rootfs}/install/$archvar.pkglist
	done

	local manifestpfx="install"
	local extraopt=""
	if [ "$insttype" = "attemptonly" ] ; then
		manifestpfx="install_attemptonly"
		extraopt="-i"
	fi

	rpmresolve $extraopt ${confbase}-base_archs.conf ${target_rootfs}/install/base_archs.pkglist -o ${target_rootfs}/install/${manifestpfx}.manifest
	if [ -s ${target_rootfs}/install/ml_archs.pkglist ] ; then
		rpmresolve $extraopt ${confbase}-ml_archs.conf ${target_rootfs}/install/ml_archs.pkglist -o ${target_rootfs}/install/${manifestpfx}_multilib.manifest
	fi
}

#
# Install a bunch of packages using rpm.
# There are 3 solutions in an image's FRESH generation:
# 1) initial_solution
# 2) total_solution
# 3) COMPLEMENTARY solution
#
# It is different when incremental image generation is enabled in the
# SECOND generation:
# 1) The initial_solution is skipped.
# 2) The incremental image generation takes action during the total_solution
#    installation, the previous installed COMPLEMENTARY pkgs usually would be
#    removed here, the new COMPLEMENTARY ones would be installed in the next
#    step.
# 3) The COMPLEMENTARY would always be installed since it is
#    generated based on the second step's image.
#
# the following shell variables needs to be set before calling this func:
# INSTALL_ROOTFS_RPM - install root dir
# INSTALL_PLATFORM_RPM - main platform
# INSTALL_PLATFORM_EXTRA_RPM - extra platform
# INSTALL_CONFBASE_RPM - configuration file base name
# INSTALL_PACKAGES_RPM - packages to be installed
# INSTALL_PACKAGES_ATTEMPTONLY_RPM - packages attemped to be installed only
# INSTALL_PACKAGES_LINGUAS_RPM - additional packages for uclibc
# INSTALL_PROVIDENAME_RPM - content for provide name
# INSTALL_TASK_RPM - task name
# INSTALL_COMPLEMENTARY_RPM - 1 to enable complementary package install mode

package_install_internal_rpm () {

	local target_rootfs="${INSTALL_ROOTFS_RPM}"
	local platform="${INSTALL_PLATFORM_RPM}"
	local platform_extra="${INSTALL_PLATFORM_EXTRA_RPM}"
	local confbase="${INSTALL_CONFBASE_RPM}"
	local package_to_install="${INSTALL_PACKAGES_RPM}"
	local package_attemptonly="${INSTALL_PACKAGES_ATTEMPTONLY_RPM}"
	local package_linguas="${INSTALL_PACKAGES_LINGUAS_RPM}"
	local providename="${INSTALL_PROVIDENAME_RPM}"
	local task="${INSTALL_TASK_RPM}"

	if [ "${INSTALL_COMPLEMENTARY_RPM}" != "1" ] ; then
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
	else
		mv ${target_rootfs}/install/total_solution.manifest ${target_rootfs}/install/original_solution.manifest
	fi

	# Setup manifest of packages to install...
	mkdir -p ${target_rootfs}/install
	rm -f ${target_rootfs}/install/install.manifest

	# Uclibc builds don't provide this stuff...
	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then
		if [ ! -z "${package_linguas}" ]; then
			process_pkg_list_rpm linguas ${package_linguas}
		fi
	fi

	if [ ! -z "${package_to_install}" ]; then
		process_pkg_list_rpm default ${package_to_install}
	fi

	# Normal package installation

	# Generate an install solution by doing a --justdb install, then recreate it with
	# an actual package install!
	if [ -s ${target_rootfs}/install/install.manifest ]; then
		echo "# Install manifest padding" >> ${target_rootfs}/install/install.manifest
		${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
			--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
			--root "${target_rootfs}/install" \
			-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}-base_archs.macro`" \
			-D "__dbi_txn create nofsync" \
			-U --justdb --replacepkgs --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
			${target_rootfs}/install/install.manifest
	fi

	if [ ! -z "${package_attemptonly}" ]; then
		echo "Adding attempt only packages..."
		process_pkg_list_rpm attemptonly ${package_attemptonly}
		cat ${target_rootfs}/install/install_attemptonly.manifest | while read pkg_name
		do
			echo "Attempting $pkg_name..." >> "`dirname ${BB_LOGFILE}`/log.do_${task}_attemptonly.${PID}"
			${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
				--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
				--root "${target_rootfs}/install" \
				-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}.macro`" \
				-D "__dbi_txn create nofsync private" \
				-U --justdb --replacepkgs --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
			$pkg_name >> "`dirname ${BB_LOGFILE}`/log.do_${task}_attemptonly.${PID}" 2>&1 || true
		done
	fi

	#### Note: 'Recommends' is an arbitrary tag that means _SUGGESTS_ in OE-core..
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
			--root "${target_rootfs}/install" \
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
			found=0
			for archvar in base_archs ml_archs ; do
				pkg_name=$(resolve_package_rpm ${confbase}-${archvar}.conf ${pkg})
				if [ -n "$pkg_name" ]; then
					found=1
					break
				fi
			done

			if [ $found -eq 0 ]; then
				echo "Note: Unable to find package $pkg -- suggests"
				echo "Unable to find package $pkg." >> "`dirname ${BB_LOGFILE}`/log.do_${task}_recommend.${PID}"
				continue
			fi
			echo "Attempting $pkg_name..." >> "`dirname ${BB_LOGFILE}`/log.do_{task}_recommend.${PID}"
			${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
				--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
				--root "${target_rootfs}/install" \
				-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}.macro`" \
				-D "__dbi_txn create nofsync private" \
				-U --justdb --replacepkgs --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
				$pkg_name >> "`dirname ${BB_LOGFILE}`/log.do_${task}_recommend.${PID}" 2>&1 || true
		done
		cat ${target_rootfs}/install/recommend.list ${target_rootfs}/install/recommend.new | sort -u > ${target_rootfs}/install/recommend.new.list
		mv -f ${target_rootfs}/install/recommend.new.list ${target_rootfs}/install/recommend.list
		rm ${target_rootfs}/install/recommend ${target_rootfs}/install/recommend.new
	 done
	fi

	# Now that we have a solution, pull out a list of what to install...
	echo "Manifest: ${target_rootfs}/install/install.manifest"
	${RPM} -D "_dbpath ${target_rootfs}/install" -qa --qf "%{packageorigin}\n" \
		--root "${target_rootfs}/install" \
		-D "__dbi_txn create nofsync private" \
		> ${target_rootfs}/install/install_solution.manifest

	touch ${target_rootfs}/install/install_multilib_solution.manifest

	if [ -s "${target_rootfs}/install/install_multilib.manifest" ]; then
		# multilib package installation
		echo "# Install multilib manifest padding" >> ${target_rootfs}/install/install_multilib.manifest

		# Generate an install solution by doing a --justdb install, then recreate it with
		# an actual package install!
		${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
			--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
			--root "${target_rootfs}/install" \
			-D "_dbpath ${target_rootfs}/install" -D "`cat ${confbase}-ml_archs.macro`" \
			-D "__dbi_txn create nofsync" \
			-U --justdb --replacepkgs --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
			${target_rootfs}/install/install_multilib.manifest

		# Now that we have a solution, pull out a list of what to install...
		echo "Manifest: ${target_rootfs}/install/install_multilib.manifest"
		${RPM} -D "_dbpath ${target_rootfs}/install" -qa --qf "%{packageorigin}\n" \
			--root "${target_rootfs}/install" \
			-D "__dbi_txn create nofsync private" \
			> ${target_rootfs}/install/install_multilib_solution.manifest

	fi

	cat ${target_rootfs}/install/install_solution.manifest > ${target_rootfs}/install/total_solution.manifest
	cat ${target_rootfs}/install/install_multilib_solution.manifest >> ${target_rootfs}/install/total_solution.manifest

	# Construct install scriptlet wrapper
	cat << EOF > ${WORKDIR}/scriptlet_wrapper
#!/bin/bash

export PATH="${PATH}"
export D="${target_rootfs}"
export OFFLINE_ROOT="\$D"
export IPKG_OFFLINE_ROOT="\$D"
export OPKG_OFFLINE_ROOT="\$D"

\$2 \$1/\$3 \$4
if [ \$? -ne 0 ]; then
  mkdir -p \$1/etc/rpm-postinsts
  num=100
  while [ -e \$1/etc/rpm-postinsts/\${num} ]; do num=\$((num + 1)); done
  echo "#!\$2" > \$1/etc/rpm-postinsts/\${num}
  echo "# Arg: \$4" >> \$1/etc/rpm-postinsts/\${num}
  cat \$1/\$3 >> \$1/etc/rpm-postinsts/\${num}
  chmod +x \$1/etc/rpm-postinsts/\${num}
fi
EOF

	chmod 0755 ${WORKDIR}/scriptlet_wrapper

	# Configure RPM... we enforce these settings!
	mkdir -p ${target_rootfs}${rpmlibdir}
	mkdir -p ${target_rootfs}${rpmlibdir}/log
	# After change the __db.* cache size, log file will not be generated automatically,
	# that will raise some warnings, so touch a bare log for rpm write into it.
	touch ${target_rootfs}${rpmlibdir}/log/log.0000000001
	cat > ${target_rootfs}${rpmlibdir}/DB_CONFIG << EOF
# ================ Environment
set_data_dir .
set_create_dir .
set_lg_dir ./log
set_tmp_dir ./tmp
set_flags db_log_autoremove on

# -- thread_count must be >= 8
set_thread_count 64

# ================ Logging

# ================ Memory Pool
set_cachesize 0 1048576 0
set_mp_mmapsize 268435456

# ================ Locking
set_lk_max_locks 16384
set_lk_max_lockers 16384
set_lk_max_objects 16384
mutex_set_max 163840

# ================ Replication
EOF

	if [ "${INSTALL_COMPLEMENTARY_RPM}" = "1" ] ; then
		# Only install packages not already installed (dependency calculation will
		# almost certainly have added some that have been)
		sort ${target_rootfs}/install/original_solution.manifest > ${target_rootfs}/install/original_solution_sorted.manifest
		sort ${target_rootfs}/install/total_solution.manifest > ${target_rootfs}/install/total_solution_sorted.manifest
		comm -2 -3 ${target_rootfs}/install/total_solution_sorted.manifest \
			${target_rootfs}/install/original_solution_sorted.manifest > \
			${target_rootfs}/install/diff.manifest
		mv ${target_rootfs}/install/diff.manifest ${target_rootfs}/install/total_solution.manifest
	elif [ "${INC_RPM_IMAGE_GEN}" = "1" -a -d "${target_rootfs}${rpmlibdir}" ]; then
		echo "Skipping pre install due to existing image"
	else
		# RPM is special. It can't handle dependencies and preinstall scripts correctly. Its
		# probably a feature. The only way to convince rpm to actually run the preinstall scripts
		# for base-passwd and shadow first before installing packages that depend on these packages
		# is to do two image installs, installing one set of packages, then the other.
		rm -f ${target_rootfs}/install/initial_install.manifest
		echo "Installing base dependencies first (base-passwd, base-files and shadow) since rpm is special"
		grep /base-passwd-[0-9] ${target_rootfs}/install/total_solution.manifest >> ${target_rootfs}/install/initial_install.manifest || true
		grep /base-files-[0-9] ${target_rootfs}/install/total_solution.manifest >> ${target_rootfs}/install/initial_install.manifest || true
		grep /shadow-[0-9] ${target_rootfs}/install/total_solution.manifest >> ${target_rootfs}/install/initial_install.manifest || true

		if [ -s ${target_rootfs}/install/initial_install.manifest ]; then
			echo "# Initial Install manifest padding..." >> ${target_rootfs}/install/initial_install.manifest

			# Generate an install solution by doing a --justdb install, then recreate it with
			# an actual package install!
			mkdir -p ${target_rootfs}/initial

			${RPM} --predefine "_rpmds_sysinfo_path ${target_rootfs}/etc/rpm/sysinfo" \
				--predefine "_rpmrc_platform_path ${target_rootfs}/etc/rpm/platform" \
				--root "${target_rootfs}/install" \
				-D "_dbpath ${target_rootfs}/initial" -D "`cat ${confbase}.macro`" \
				-D "__dbi_txn create nofsync" \
				-U --justdb --replacepkgs --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
				${target_rootfs}/install/initial_install.manifest

			${RPM} -D "_dbpath ${target_rootfs}/initial" -qa --qf "%{packageorigin}\n" \
				-D "__dbi_txn create nofsync private" \
				--root "${target_rootfs}/install" \
				> ${target_rootfs}/install/initial_solution.manifest

			rpm_update_pkg ${target_rootfs}/install/initial_solution.manifest
		
			grep -Fv -f ${target_rootfs}/install/initial_solution.manifest ${target_rootfs}/install/total_solution.manifest > ${target_rootfs}/install/total_solution.manifest.new
			mv ${target_rootfs}/install/total_solution.manifest.new ${target_rootfs}/install/total_solution.manifest
		
			rm -rf ${target_rootfs}/initial
		fi
	fi

	echo "Installing main solution manifest (${target_rootfs}/install/total_solution.manifest)"

	rpm_update_pkg ${target_rootfs}/install/total_solution.manifest
}

python write_specfile () {
    import textwrap
    import oe.packagedata

    # append information for logs and patches to %prep
    def add_prep(d,spec_files_bottom):
        if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
            spec_files_bottom.append('%%prep -n %s' % d.getVar('PN', True) )
            spec_files_bottom.append('%s' % "echo \"include logs and patches, Please check them in SOURCES\"")
            spec_files_bottom.append('')

    # append the name of tarball to key word 'SOURCE' in xxx.spec.
    def tail_source(d):
        if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
            source_list = get_package(d)
            source_number = 0
            workdir = d.getVar('WORKDIR', True)
            for source in source_list:
                # The rpmbuild doesn't need the root permission, but it needs
                # to know the file's user and group name, the only user and
                # group in fakeroot is "root" when working in fakeroot.
                os.chown("%s/%s" % (workdir, source), 0, 0)
                spec_preamble_top.append('Source' + str(source_number) + ': %s' % source)
                source_number += 1
    # We need a simple way to remove the MLPREFIX from the package name,
    # and dependency information...
    def strip_multilib(name, d):
        multilibs = d.getVar('MULTILIBS', True) or ""
        for ext in multilibs.split():
            eext = ext.split(':')
            if len(eext) > 1 and eext[0] == 'multilib' and name and name.find(eext[1] + '-') >= 0:
                name = "".join(name.split(eext[1] + '-'))
        return name

#        ml = d.getVar("MLPREFIX", True)
#        if ml and name and len(ml) != 0 and name.find(ml) == 0:
#            return ml.join(name.split(ml, 1)[1:])
#        return name

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
        depends = d.getVar(varname, True)
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
                newdeps_dict[dep] = ver
            depends = bb.utils.join_deps(newdeps_dict)
            d.setVar(varname, depends.strip())

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
        for rootpath, dirs, files in os.walk(walkpath):
            path = rootpath.replace(walkpath, "")
            for dir in dirs:
                # All packages own the directories their files are in...
                target.append('%dir "' + path + '/' + dir + '"')
            for file in files:
                if conffiles.count(path + '/' + file):
                    target.append('%config "' + path + '/' + file + '"')
                else:
                    target.append('"' + path + '/' + file + '"')

    # Prevent the prerm/postrm scripts from being run during an upgrade
    def wrap_uninstall(scriptvar):
        scr = scriptvar.strip()
        if scr.startswith("#!"):
            pos = scr.find("\n") + 1
        else:
            pos = 0
        scr = scr[:pos] + 'if [ "$1" = "0" ] ; then\n' + scr[pos:] + '\nfi'
        return scr

    packages = d.getVar('PACKAGES', True)
    if not packages or packages == '':
        bb.debug(1, "No packages; nothing to do")
        return

    pkgdest = d.getVar('PKGDEST', True)
    if not pkgdest:
        bb.fatal("No PKGDEST")
        return

    outspecfile = d.getVar('OUTSPECFILE', True)
    if not outspecfile:
        bb.fatal("No OUTSPECFILE")
        return

    # Construct the SPEC file...
    srcname    = strip_multilib(d.getVar('PN', True), d)
    srcsummary = (d.getVar('SUMMARY', True) or d.getVar('DESCRIPTION', True) or ".")
    srcversion = d.getVar('PKGV', True).replace('-', '+')
    srcrelease = d.getVar('PKGR', True)
    srcepoch   = (d.getVar('PKGE', True) or "")
    srclicense = d.getVar('LICENSE', True)
    srcsection = d.getVar('SECTION', True)
    srcmaintainer  = d.getVar('MAINTAINER', True)
    srchomepage    = d.getVar('HOMEPAGE', True)
    srcdescription = d.getVar('DESCRIPTION', True) or "."

    srcdepends     = strip_multilib(d.getVar('DEPENDS', True), d)
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

        localdata.setVar('ROOT', '')
        localdata.setVar('ROOT_%s' % pkg, root)
        pkgname = localdata.getVar('PKG_%s' % pkg, True)
        if not pkgname:
            pkgname = pkg
        localdata.setVar('PKG', pkgname)

        localdata.setVar('OVERRIDES', pkg)

        bb.data.update_data(localdata)

        conffiles = (localdata.getVar('CONFFILES', True) or "").split()

        splitname    = strip_multilib(pkgname, d)

        splitsummary = (localdata.getVar('SUMMARY', True) or localdata.getVar('DESCRIPTION', True) or ".")
        splitversion = (localdata.getVar('PKGV', True) or "").replace('-', '+')
        splitrelease = (localdata.getVar('PKGR', True) or "")
        splitepoch   = (localdata.getVar('PKGE', True) or "")
        splitlicense = (localdata.getVar('LICENSE', True) or "")
        splitsection = (localdata.getVar('SECTION', True) or "")
        splitdescription = (localdata.getVar('DESCRIPTION', True) or ".")

        translate_vers('RDEPENDS', localdata)
        translate_vers('RRECOMMENDS', localdata)
        translate_vers('RSUGGESTS', localdata)
        translate_vers('RPROVIDES', localdata)
        translate_vers('RREPLACES', localdata)
        translate_vers('RCONFLICTS', localdata)

        # Map the dependencies into their final form
        mapping_rename_hook(localdata)

        splitrdepends    = strip_multilib(localdata.getVar('RDEPENDS', True), d) or ""
        splitrrecommends = strip_multilib(localdata.getVar('RRECOMMENDS', True), d) or ""
        splitrsuggests   = strip_multilib(localdata.getVar('RSUGGESTS', True), d) or ""
        splitrprovides   = strip_multilib(localdata.getVar('RPROVIDES', True), d) or ""
        splitrreplaces   = strip_multilib(localdata.getVar('RREPLACES', True), d) or ""
        splitrconflicts  = strip_multilib(localdata.getVar('RCONFLICTS', True), d) or ""
        splitrobsoletes  = []

        # Gather special src/first package data
        if srcname == splitname:
            srcrdepends    = splitrdepends
            srcrrecommends = splitrrecommends
            srcrsuggests   = splitrsuggests
            srcrprovides   = splitrprovides
            srcrreplaces   = splitrreplaces
            srcrconflicts  = splitrconflicts

            srcpreinst  = localdata.getVar('pkg_preinst', True)
            srcpostinst = localdata.getVar('pkg_postinst', True)
            srcprerm    = localdata.getVar('pkg_prerm', True)
            srcpostrm   = localdata.getVar('pkg_postrm', True)

            file_list = []
            walk_files(root, file_list, conffiles)
            if not file_list and localdata.getVar('ALLOW_EMPTY') != "1":
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

        print_deps(splitrdepends, "Requires", spec_preamble_bottom, d)
        # Suggests in RPM are like recommends in OE-core!
        print_deps(splitrrecommends, "Suggests", spec_preamble_bottom, d)
        # While there is no analog for suggests... (So call them recommends for now)
        print_deps(splitrsuggests,  "Recommends", spec_preamble_bottom, d)
        print_deps(splitrprovides,  "Provides", spec_preamble_bottom, d)
        print_deps(splitrobsoletes, "Obsoletes", spec_preamble_bottom, d)

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

        print_deps(splitrconflicts,  "Conflicts", spec_preamble_bottom, d)

        spec_preamble_bottom.append('')

        spec_preamble_bottom.append('%%description -n %s' % splitname)
        dedent_text = textwrap.dedent(splitdescription).strip()
        spec_preamble_bottom.append('%s' % textwrap.fill(dedent_text, width=75))

        spec_preamble_bottom.append('')

        # Now process scriptlets
        for script in ["preinst", "postinst", "prerm", "postrm"]:
            scriptvar = localdata.getVar('pkg_%s' % script, True)
            if not scriptvar:
                continue
            if script == 'preinst':
                spec_scriptlets_bottom.append('%%pre -n %s' % splitname)
            elif script == 'postinst':
                spec_scriptlets_bottom.append('%%post -n %s' % splitname)
            elif script == 'prerm':
                spec_scriptlets_bottom.append('%%preun -n %s' % splitname)
                scriptvar = wrap_uninstall(scriptvar)
            elif script == 'postrm':
                spec_scriptlets_bottom.append('%%postun -n %s' % splitname)
                scriptvar = wrap_uninstall(scriptvar)
            spec_scriptlets_bottom.append('# %s - %s' % (splitname, script))
            spec_scriptlets_bottom.append(scriptvar)
            spec_scriptlets_bottom.append('')

        # Now process files
        file_list = []
        walk_files(root, file_list, conffiles)
        if not file_list and localdata.getVar('ALLOW_EMPTY') != "1":
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
    
    add_prep(d,spec_files_bottom)
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
    tail_source(d)

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

    print_deps(srcdepends, "BuildRequires", spec_preamble_top, d)
    print_deps(srcrdepends, "Requires", spec_preamble_top, d)
    # Suggests in RPM are like recommends in OE-core!
    print_deps(srcrrecommends, "Suggests", spec_preamble_top, d)
    # While there is no analog for suggests... (So call them recommends for now)
    print_deps(srcrsuggests, "Recommends", spec_preamble_top, d)
    print_deps(srcrprovides, "Provides", spec_preamble_top, d)
    print_deps(srcrobsoletes, "Obsoletes", spec_preamble_top, d)
    
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

    print_deps(srcrconflicts, "Conflicts", spec_preamble_top, d)

    spec_preamble_top.append('')

    spec_preamble_top.append('%description')
    dedent_text = textwrap.dedent(srcdescription).strip()
    spec_preamble_top.append('%s' % textwrap.fill(dedent_text, width=75))

    spec_preamble_top.append('')

    if srcpreinst:
        spec_scriptlets_top.append('%pre')
        spec_scriptlets_top.append('# %s - preinst' % srcname)
        spec_scriptlets_top.append(srcpreinst)
        spec_scriptlets_top.append('')
    if srcpostinst:
        spec_scriptlets_top.append('%post')
        spec_scriptlets_top.append('# %s - postinst' % srcname)
        spec_scriptlets_top.append(srcpostinst)
        spec_scriptlets_top.append('')
    if srcprerm:
        spec_scriptlets_top.append('%preun')
        spec_scriptlets_top.append('# %s - prerm' % srcname)
        scriptvar = wrap_uninstall(srcprerm)
        spec_scriptlets_top.append(scriptvar)
        spec_scriptlets_top.append('')
    if srcpostrm:
        spec_scriptlets_top.append('%postun')
        spec_scriptlets_top.append('# %s - postrm' % srcname)
        scriptvar = wrap_uninstall(srcpostrm)
        spec_scriptlets_top.append(scriptvar)
        spec_scriptlets_top.append('')

    # Write the SPEC file
    try:
        from __builtin__ import file
        specfile = file(outspecfile, 'w')
    except OSError:
        raise bb.build.FuncFailed("unable to open spec file for writing.")

    # RPMSPEC_PREAMBLE is a way to add arbitrary text to the top
    # of the generated spec file
    external_preamble = d.getVar("RPMSPEC_PREAMBLE", True)
    if external_preamble:
        specfile.write(external_preamble + "\n")

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
    def creat_srpm_dir(d):
        if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
            clean_licenses = get_licenses(d)
            pkgwritesrpmdir = bb.data.expand('${PKGWRITEDIRSRPM}/${PACKAGE_ARCH_EXTEND}', d)
            pkgwritesrpmdir = pkgwritesrpmdir + '/' + clean_licenses
            bb.mkdirhier(pkgwritesrpmdir)
            os.chmod(pkgwritesrpmdir, 0755)
            return pkgwritesrpmdir
            
    # We need a simple way to remove the MLPREFIX from the package name,
    # and dependency information...
    def strip_multilib(name, d):
        ml = d.getVar("MLPREFIX", True)
        if ml and name and len(ml) != 0 and name.find(ml) >= 0:
            return "".join(name.split(ml))
        return name

    workdir = d.getVar('WORKDIR', True)
    outdir = d.getVar('DEPLOY_DIR_IPK', True)
    tmpdir = d.getVar('TMPDIR', True)
    pkgd = d.getVar('PKGD', True)
    pkgdest = d.getVar('PKGDEST', True)
    if not workdir or not outdir or not pkgd or not tmpdir:
        bb.error("Variables incorrectly set, unable to package")
        return

    packages = d.getVar('PACKAGES', True)
    if not packages or packages == '':
        bb.debug(1, "No packages; nothing to do")
        return

    # Construct the spec file...
    srcname    = strip_multilib(d.getVar('PN', True), d)
    outspecfile = workdir + "/" + srcname + ".spec"
    d.setVar('OUTSPECFILE', outspecfile)
    bb.build.exec_func('write_specfile', d)

    # Construct per file dependencies file
    def dump_filerdeps(varname, outfile, d):
        outfile.write("#!/usr/bin/env python\n\n")
        outfile.write("# Dependency table\n")
        outfile.write('deps = {\n')
        for pkg in packages.split():
            dependsflist_key = 'FILE' + varname + 'FLIST' + "_" + pkg
            dependsflist = (d.getVar(dependsflist_key, True) or "")
            for dfile in dependsflist.split():
                key = "FILE" + varname + "_" + dfile + "_" + pkg
                depends_dict = bb.utils.explode_dep_versions(d.getVar(key, True) or "")
                file = dfile.replace("@underscore@", "_")
                file = file.replace("@closebrace@", "]")
                file = file.replace("@openbrace@", "[")
                file = file.replace("@tab@", "\t")
                file = file.replace("@space@", " ")
                file = file.replace("@at@", "@")
                outfile.write('"' + pkgd + file + '" : "')
                for dep in depends_dict:
                    ver = depends_dict[dep]
                    if dep and ver:
                        ver = ver.replace("(","")
                        ver = ver.replace(")","")
                        outfile.write(dep + " " + ver + " ")
                    else:
                        outfile.write(dep + " ")
                outfile.write('",\n')
        outfile.write('}\n\n')
        outfile.write("import sys\n")
        outfile.write("while 1:\n")
        outfile.write("\tline = sys.stdin.readline().strip()\n")
        outfile.write("\tif not line:\n")
        outfile.write("\t\tsys.exit(0)\n")
        outfile.write("\tif line in deps:\n")
        outfile.write("\t\tprint(deps[line] + '\\n')\n")

    # OE-core dependencies a.k.a. RPM requires
    outdepends = workdir + "/" + srcname + ".requires"

    try:
        from __builtin__ import file
        dependsfile = file(outdepends, 'w')
    except OSError:
        raise bb.build.FuncFailed("unable to open spec file for writing.")

    dump_filerdeps('RDEPENDS', dependsfile, d)

    dependsfile.close()
    os.chmod(outdepends, 0755)

    # OE-core / RPM Provides
    outprovides = workdir + "/" + srcname + ".provides"

    try:
        from __builtin__ import file
        providesfile = file(outprovides, 'w')
    except OSError:
        raise bb.build.FuncFailed("unable to open spec file for writing.")

    dump_filerdeps('RPROVIDES', providesfile, d)

    providesfile.close()
    os.chmod(outprovides, 0755)

    # Setup the rpmbuild arguments...
    rpmbuild = d.getVar('RPMBUILD', True)
    targetsys = d.getVar('TARGET_SYS', True)
    targetvendor = d.getVar('TARGET_VENDOR', True)
    package_arch = d.getVar('PACKAGE_ARCH', True) or ""
    if package_arch not in "all any noarch".split() and not package_arch.endswith("-nativesdk"):
        ml_prefix = (d.getVar('MLPREFIX', True) or "").replace("-", "_")
        d.setVar('PACKAGE_ARCH_EXTEND', ml_prefix + package_arch)
    else:
        d.setVar('PACKAGE_ARCH_EXTEND', package_arch)
    pkgwritedir = d.expand('${PKGWRITEDIRRPM}/${PACKAGE_ARCH_EXTEND}')
    pkgarch = d.expand('${PACKAGE_ARCH_EXTEND}${TARGET_VENDOR}-${TARGET_OS}')
    magicfile = d.expand('${STAGING_DIR_NATIVE}${datadir_native}/misc/magic.mgc')
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
    cmd = cmd + " --define '_tmppath " + workdir + "'"
    if d.getVar('SOURCE_ARCHIVE_PACKAGE_TYPE', True) == 'srpm':
        cmd = cmd + " --define '_sourcedir " + workdir + "'"
        cmdsrpm = cmd + " --define '_srcrpmdir " + creat_srpm_dir(d) + "'"
        cmdsrpm = cmdsrpm + " -bs " + outspecfile
        # Build the .src.rpm
        d.setVar('SBUILDSPEC', cmdsrpm + "\n")
        d.setVarFlag('SBUILDSPEC', 'func', '1')
        bb.build.exec_func('SBUILDSPEC', d)
        # Remove the source (SOURCE0, SOURCE1 ...)
        cmd = cmd + " --rmsource "
    cmd = cmd + " -bb " + outspecfile

    # Build the rpm package!
    d.setVar('BUILDSPEC', cmd + "\n")
    d.setVarFlag('BUILDSPEC', 'func', '1')
    bb.build.exec_func('BUILDSPEC', d)
}

python () {
    if d.getVar('PACKAGES', True) != '':
        deps = ' rpm-native:do_populate_sysroot virtual/fakeroot-native:do_populate_sysroot'
        d.appendVarFlag('do_package_write_rpm', 'depends', deps)
        d.setVarFlag('do_package_write_rpm', 'fakeroot', 1)
        d.setVarFlag('do_package_write_rpm_setscene', 'fakeroot', 1)
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
do_package_write_rpm[umask] = "022"
addtask package_write_rpm before do_package_write after do_package

PACKAGEINDEXES += "package_update_index_rpm; [ ! -e ${DEPLOY_DIR_RPM} ] || createrepo ${DEPLOY_DIR_RPM};"
PACKAGEINDEXDEPS += "rpm-native:do_populate_sysroot"
PACKAGEINDEXDEPS += "createrepo-native:do_populate_sysroot"
