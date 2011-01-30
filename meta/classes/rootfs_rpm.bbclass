#
# Creates a root filesystem out of rpm packages
#

ROOTFS_PKGMANAGE = "rpm zypper"
# Postinstalls on device are handled within this class at present
ROOTFS_PKGMANAGE_BOOTSTRAP = ""

do_rootfs[depends] += "rpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_rootfs[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
do_rootfs[depends] += "createrepo-native:do_populate_sysroot"

do_rootfs[recrdeptask] += "do_package_write_rpm"

AWKPOSTINSTSCRIPT = "${POKYBASE}/scripts/rootfs_rpm-extract-postinst.awk"

RPM_PREPROCESS_COMMANDS = "package_update_index_rpm; package_generate_rpm_conf; "
RPM_POSTPROCESS_COMMANDS = ""

# To test the install_all_locales.. enable the following...
#RPM_POSTPROCESS_COMMANDS = "install_all_locales; "
#
#IMAGE_LOCALES="en-gb"

rpmlibdir = "/var/lib/rpm"
opkglibdir = "${localstatedir}/lib/opkg"

RPMOPTS="--dbpath ${rpmlibdir} --define='_openall_before_chroot 1'"
RPM="rpm ${RPMOPTS}"

# RPM doesn't work with multiple rootfs generation at once due to collisions in the use of files 
# in ${DEPLOY_DIR_RPM}. This can be removed if package_update_index_rpm can be called concurrently
do_rootfs[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

fakeroot rootfs_rpm_do_rootfs () {
	set +x

	${RPM_PREPROCESS_COMMANDS}

	createrepo "${DEPLOY_DIR_RPM}"

	# Setup base system configuration
	mkdir -p ${IMAGE_ROOTFS}/etc/rpm/

	# Default arch is the top..
	echo "${TARGET_ARCH}-unknown-linux" >${IMAGE_ROOTFS}/etc/rpm/platform
	# Add the rest in sort order..
	for each in ${PACKAGE_ARCHS} ; do
		echo "$each""-unknown-linux" >>${IMAGE_ROOTFS}/etc/rpm/platform
	done

	# Tell RPM that the "/" directory exist and is available
	mkdir -p ${IMAGE_ROOTFS}/etc/rpm/sysinfo
	echo "/" >${IMAGE_ROOTFS}/etc/rpm/sysinfo/Dirnames

	# Setup manifest of packages to install...
	mkdir -p ${IMAGE_ROOTFS}/install
	echo "# Install manifest" > ${IMAGE_ROOTFS}/install/install.manifest

	# Uclibc builds don't provide this stuff...
	if [ x${TARGET_OS} = "xlinux" ] || [ x${TARGET_OS} = "xlinux-gnueabi" ] ; then 
		if [ ! -z "${LINGUAS_INSTALL}" ]; then
			for pkg in ${LINGUAS_INSTALL}; do
				echo "Processing $pkg..."
				pkg_name=$(resolve_package $pkg)
				if [ -z "$pkg_name" ]; then
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
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg!"
				exit 1
			fi
			echo $pkg_name >> ${IMAGE_ROOTFS}/install/install.manifest
		done
	fi

	# Generate an install solution by doing a --justdb install, then recreate it with
	# an actual package install!
	${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
		-D "__dbi_txn create nofsync" \
		-U --justdb --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
		${IMAGE_ROOTFS}/install/install.manifest

	if [ ! -z "${PACKAGE_INSTALL_ATTEMPTONLY}" ]; then
		echo "Adding attempt only packages..."
		for pkg in ${PACKAGE_INSTALL_ATTEMPTONLY} ; do
			echo "Processing $pkg..."
			pkg_name=$(resolve_package $pkg)
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg!"
				exit 1
			fi
			echo "Attempting $pkg_name..." >> "${WORKDIR}/temp/log.do_rootfs_attemptonly.${PID}"
			${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
				-D "__dbi_txn create nofsync private" \
				-U --justdb --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
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
			-D "__dbi_txn create nofsync private" \
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
			pkg_name=$(resolve_package $pkg)
			if [ -z "$pkg_name" ]; then
				echo "Unable to find package $pkg." >> "${WORKDIR}/temp/log.do_rootfs_recommend.${PID}"
				continue
			fi
			echo "Attempting $pkg_name..." >> "${WORKDIR}/temp/log.do_rootfs_recommend.${PID}"
			${RPM} -D "_dbpath ${IMAGE_ROOTFS}/install" -D "`cat ${DEPLOY_DIR_RPM}/solvedb.macro`" \
				-D "__dbi_txn create nofsync private" \
				-U --justdb --noscripts --notriggers --noparentdirs --nolinktos --ignoresize \
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
		-D "__dbi_txn create nofsync private" \
		| grep -i 'Packageorigin' | cut -d : -f 2 > ${IMAGE_ROOTFS}/install/install_solution.manifest

	# Attempt install
	${RPM} --root ${IMAGE_ROOTFS} -D "_dbpath ${rpmlibdir}" \
		--noscripts --notriggers --noparentdirs --nolinktos \
		-D "__dbi_txn create nofsync private" \
		-Uhv ${IMAGE_ROOTFS}/install/install_solution.manifest

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	${ROOTFS_POSTINSTALL_COMMAND}

	mkdir -p ${IMAGE_ROOTFS}/etc/rpm-postinsts/
	${RPM} --root ${IMAGE_ROOTFS} -D '_dbpath ${rpmlibdir}' -qa \
		-D "__dbi_txn create nofsync private" \
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

	# Move manifests into the directory with the logs
	mv ${IMAGE_ROOTFS}/install/*.manifest ${T}/

	# Remove all remaining resolver files
	rm -rf ${IMAGE_ROOTFS}/install

	log_check rootfs

	# Workaround so the parser knows we need the resolve_package function!
	if false ; then
		resolve_package foo || true
	fi
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${rpmlibdir}
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
}

# Resolve package names to filepaths
resolve_package() {
	pkg="$1"
	pkg_name=""
	for solve in `cat ${DEPLOY_DIR_RPM}/solvedb.conf`; do
		pkg_name=$(${RPM} -D "_dbpath $solve" -D "__dbi_txn create nofsync" -q --yaml $pkg | grep -i 'Packageorigin' | cut -d : -f 2)
		if [ -n "$pkg_name" ]; then
			break;
		fi
	done
	echo $pkg_name
}	

install_all_locales() {
	PACKAGES_TO_INSTALL=""

	# Generate list of installed packages...
	INSTALLED_PACKAGES=$( \
		${RPM} --root ${IMAGE_ROOTFS} -D "_dbpath ${rpmlibdir}" \
		-D "__dbi_txn create nofsync private" \
		-qa --qf "[%{NAME}\n]" | egrep -v -- "(-locale-|-dev$|-doc$|^kernel|^glibc|^ttf|^task|^perl|^python)" \
	)

	# This would likely be faster if we did it in one transaction
	# but this should be good enough for the few users of this function...
	for pkg in $INSTALLED_PACKAGES; do
		for lang in ${IMAGE_LOCALES}; do
			pkg_name=$(resolve_package $pkg-locale-$lang)
			if [ -n "$pkg_name" ]; then
				${RPM} --root ${IMAGE_ROOTFS} -D "_dbpath ${rpmlibdir}" \
					-D "__dbi_txn create nofsync private" \
					--noscripts --notriggers --noparentdirs --nolinktos \
					-Uhv $pkg_name || true
			fi
		done
	done
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
