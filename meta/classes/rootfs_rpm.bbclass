#
# Creates a root filesystem out of rpm packages
#

ROOTFS_PKGMANAGE = "rpm" 
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "rpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_rootfs[depends] += "opkg-native:do_populate_sysroot"

do_rootfs[recrdeptask] += "do_package_write_rpm"

AWKPOSTINSTSCRIPT = "${POKYBASE}/scripts/rootfs_rpm-extract-postinst.awk"

RPM_PREPROCESS_COMMANDS = "package_update_index_rpm; package_generate_rpm_conf"
RPM_POSTPROCESS_COMMANDS = ""

rpmlibdir = "/var/lib/rpm"
opkglibdir = "${localstatedir}/lib/opkg"

RPMOPTS="--dbpath ${rpmlibdir} --define='_openall_before_chroot 1'"
RPM="${BUILD_ARCH}-${BUILD_OS}-rpm ${RPMOPTS}"

fakeroot rootfs_rpm_do_rootfs () {
	set +x

	${RPM_PREPROCESS_COMMANDS}

	# Setup base system configuration
	mkdir -p ${IMAGE_ROOTFS}/etc/rpm/
	echo "${TARGET_ARCH}-linux" >${IMAGE_ROOTFS}/etc/rpm/platform

	# Setup manifest of packages to install...
	mkdir -p ${IMAGE_ROOTFS}/install
	echo "# Install manifest" > ${IMAGE_ROOTFS}/install/install.manifest

	# Uclibc builds don't provide this stuff...
	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then 
		if [ ! -z "${LINGUAS_INSTALL}" ]; then
			for pkg in ${LINGUAS_INSTALL}; do
				echo "Processing $pkg..."
				pkg_name=$(resolve_package $pkg)
				if [ -z '$pkg_name' ]; then
					echo "Unable to find package $pkg!"
					exit 1
				fi
				echo $pkg_name >> ${IMAGE_ROOTFS}/install/install.manifest
			done
		fi
	fi

	if [ ! -z "${PACKAGE_INSTALL}" ]; then
		for pkg in ${PACKAGE_INSTALL} ; do
			echo "Processing $pkg..."
			pkg_name=$(resolve_package $pkg)
			if [ -z '$pkg_name' ]; then
				echo "Unable to find package $pkg!"
				exit 1
			fi
			echo $pkg_name >> ${IMAGE_ROOTFS}/install/install.manifest
		done
	fi

	# Generate an install solution by doing a --justdb install, then recreate it with
	# an actual package install!
	${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
		-D "__dbi_cdb create mp_mmapsize=128Mb mp_size=1Mb nofsync" \
		-U --justdb --noscripts --notriggers --noparentdirs --nolinktos \
		${IMAGE_ROOTFS}/install/install.manifest

	if [ ! -z "${PACKAGE_INSTALL_ATTEMPTONLY}" ]; then
		echo "Adding attempt only packages..."
		for pkg in ${PACKAGE_INSTALL_ATTEMPTONLY} ; do
			echo "Processing $pkg..."
			pkg_name=$(resolve_package $pkg)
			if [ -z '$pkg_name' ]; then
				echo "Unable to find package $pkg!"
				exit 1
			fi
			echo "Attempting $pkg_name..." >> "${WORKDIR}/temp/log.do_rootfs_attemptonly.${PID}"
			${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
				-D "__dbi_cdb create mp_mmapsize=128Mb mp_size=1Mb nofsync private" \
				-U --justdb --noscripts --notriggers --noparentdirs --nolinktos \
			$pkg_name >> "${WORKDIR}/temp/log.do_rootfs_attemptonly.${PID}" || true
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
	 cat /dev/null >  ${IMAGE_ROOTFS}/install/recommend.list
	 while [ $loop -eq 1 ]; do
		# Dump the full set of recommends...
		${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
			-qa --qf "[%{RECOMMENDS}\n]" | sort -u > ${IMAGE_ROOTFS}/install/recommend
		# Did we add more to the list?
		grep -v -x -F -f ${IMAGE_ROOTFS}/install/recommend.list ${IMAGE_ROOTFS}/install/recommend > ${IMAGE_ROOTFS}/install/recommend.new || true
		# We don't want to loop unless there is a change to the list!
		loop=0
		cat ${IMAGE_ROOTFS}/install/recommend.new | \
		 while read pkg ; do
			# Ohh there was a new one, we'll need to loop again...
			loop=1
			echo "Processing $pkg..."
			pkg_name=$(resolve_package $pkg || true)
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg." >> "${WORKDIR}/temp/log.do_rootfs_recommend.${PID}"
				continue
			fi
			echo "Attempting $pkg_name..." >> "${WORKDIR}/temp/log.do_rootfs_recommend.${PID}"
			${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
				-D "__dbi_cdb create mp_mmapsize=128Mb mp_size=1Mb nofsync private" \
				-U --justdb --noscripts --notriggers --noparentdirs --nolinktos \
				$pkg_name >> "${WORKDIR}/temp/log.do_rootfs_recommend.${PID}" 2>&1 || true
		done
		cat ${IMAGE_ROOTFS}/install/recommend.list ${IMAGE_ROOTFS}/install/recommend.new | sort -u > ${IMAGE_ROOTFS}/install/recommend.new.list
		mv ${IMAGE_ROOTFS}/install/recommend.new.list ${IMAGE_ROOTFS}/install/recommend.list
		rm ${IMAGE_ROOTFS}/install/recommend ${IMAGE_ROOTFS}/install/recommend.new
	 done
	fi

	# Now that we have a solution, pull out a list of what to install...
	echo "Manifest: ${IMAGE_ROOTFS}/install/install.manifest"
	${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -qa --yaml \
		| grep -i 'Packageorigin' | cut -d : -f 2 > ${IMAGE_ROOTFS}/install/install_solution.manifest

	# Attempt install
	${RPM} --root ${IMAGE_ROOTFS} -D "_dbpath ${rpmlibdir}" \
		--noscripts --notriggers --noparentdirs --nolinktos \
		-D "__dbi_cdb create mp_mmapsize=128Mb mp_size=1Mb nofsync private" \
		-Uhv ${IMAGE_ROOTFS}/install/install_solution.manifest

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	${ROOTFS_POSTINSTALL_COMMAND}

	mkdir -p ${IMAGE_ROOTFS}/etc/rpm-postinsts/
	${RPM} --root ${IMAGE_ROOTFS} -D '_dbpath ${rpmlibdir}' -qa \
		--qf 'Name: %{NAME}\n%|POSTIN?{postinstall scriptlet%|POSTINPROG?{ (using %{POSTINPROG})}|:\n%{POSTIN}\n}:{%|POSTINPROG?{postinstall program: %{POSTINPROG}\n}|}|' \
		> ${IMAGE_ROOTFS}/etc/rpm-postinsts/combined
	awk -f ${AWKPOSTINSTSCRIPT} < ${IMAGE_ROOTFS}/etc/rpm-postinsts/combined
	rm ${IMAGE_ROOTFS}/etc/rpm-postinsts/combined	

	for i in ${IMAGE_ROOTFS}/etc/rpm-postinsts/*.sh; do
		if [ -f $i ] && sh $i; then
			# rm $i
			mv $i $i.done
		fi
	done

	install -d ${IMAGE_ROOTFS}/${sysconfdir}/rcS.d
	# Stop $i getting expanded below...
	i=\$i
	cat > ${IMAGE_ROOTFS}${sysconfdir}/rcS.d/S98configure << EOF
#!/bin/sh
for i in /etc/rpm-postinsts/*.sh; do
	echo "Running postinst $i..."
	if [ -f $i ] && sh $i; then
		# rm $i
		mv $i $i.done
	else
		echo "ERROR: postinst $i failed."
	fi
done
rm -f ${sysconfdir}/rcS.d/S98configure
EOF
	chmod 0755 ${IMAGE_ROOTFS}${sysconfdir}/rcS.d/S98configure

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	${RPM_POSTPROCESS_COMMANDS}
	${ROOTFS_POSTPROCESS_COMMAND}
	
	rm -rf ${IMAGE_ROOTFS}/var/cache2/
	rm -rf ${IMAGE_ROOTFS}/var/run2/
	rm -rf ${IMAGE_ROOTFS}/var/log2/

	# remove lock files
	rm -f ${IMAGE_ROOTFS}${rpmlibdir}/__db.*

	# remove resolver files and manifests
	rm -f ${IMAGE_ROOTFS}/install/install.manifest

	log_check rootfs
}

rootfs_rpm_log_check() {
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

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${rpmlibdir}
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
}

# Resolve package names to filepaths
resolve_package() {
	pkg="$1"
	for solve in `cat ${DEPLOY_DIR_RPM}/solvedb.conf`; do
		pkg_name=$(${RPM} -D "_dbpath $solve" -D "_dbi_tags_3 Packages:Name:Basenames:Providename:Nvra" -D "__dbi_cdb create mp_mmapsize=128Mb mp_size=1Mb nofsync" -q --yaml $pkg | grep -i 'Packageorigin' | cut -d : -f 2)
		if [ -n "$pkg_name" ]; then
			break;
		fi
	done
	if [ -z "$pkg_name" ]; then
		return 1
	fi
	echo $pkg_name
	return 0
}	

install_all_locales() {
	echo "install_all_locales: not yet implemented!"
	exit 1

    PACKAGES_TO_INSTALL=""

	INSTALLED_PACKAGES=`grep ^Package: ${IMAGE_ROOTFS}${opkglibdir}/status |sed "s/^Package: //"|egrep -v -- "(-locale-|-dev$|-doc$|^kernel|^glibc|^ttf|^task|^perl|^python)"`

    for pkg in $INSTALLED_PACKAGES
    do
        for lang in ${IMAGE_LOCALES}
        do
            if [ `opkg-cl ${IPKG_ARGS} info $pkg-locale-$lang | wc -l` -gt 2 ]
            then
                    PACKAGES_TO_INSTALL="$PACKAGES_TO_INSTALL $pkg-locale-$lang"
            fi
        done
    done
    if [ "$PACKAGES_TO_INSTALL" != "" ]
    then
        opkg-cl ${IPKG_ARGS} install $PACKAGES_TO_INSTALL
    fi
}

python () {
    if bb.data.getVar('BUILD_IMAGES_FROM_FEEDS', d, True):
        flags = bb.data.getVarFlag('do_rootfs', 'recrdeptask', d)
        flags = flags.replace("do_package_write_rpm", "")
        flags = flags.replace("do_deploy", "")
        flags = flags.replace("do_populate_sysroot", "")
        bb.data.setVarFlag('do_rootfs', 'recrdeptask', flags, d)
        bb.data.setVar('RPM_PREPROCESS_COMMANDS', '', d)
        bb.data.setVar('RPM_POSTPROCESS_COMMANDS', '', d)
}
