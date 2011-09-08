#
# Creates a root filesystem out of rpm packages
#

ROOTFS_PKGMANAGE = "rpm zypper"

# Add 50Meg of extra space for zypper database space
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@base_contains("PACKAGE_INSTALL", "zypper", " + 51200", "" ,d)}"

# Postinstalls on device are handled within this class at present
ROOTFS_PKGMANAGE_BOOTSTRAP = ""

do_rootfs[depends] += "rpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_rootfs[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
#do_rootfs[depends] += "createrepo-native:do_populate_sysroot"

do_rootfs[recrdeptask] += "do_package_write_rpm"

AWKPOSTINSTSCRIPT = "${COREBASE}/scripts/rootfs_rpm-extract-postinst.awk"

RPM_PREPROCESS_COMMANDS = "package_update_index_rpm; package_generate_rpm_conf; "
RPM_POSTPROCESS_COMMANDS = ""

# To test the install_all_locales.. enable the following...
#RPM_POSTPROCESS_COMMANDS = "install_all_locales; "
#
#IMAGE_LOCALES="en-gb"

# 
# Allow distributions to alter when [postponed] package install scripts are run
#
POSTINSTALL_INITPOSITION ?= "98"

rpmlibdir = "/var/lib/rpm"
opkglibdir = "${localstatedir}/lib/opkg"

RPMOPTS="--dbpath ${rpmlibdir} --define='_openall_before_chroot 1'"
RPM="rpm ${RPMOPTS}"

# RPM doesn't work with multiple rootfs generation at once due to collisions in the use of files 
# in ${DEPLOY_DIR_RPM}. This can be removed if package_update_index_rpm can be called concurrently
do_rootfs[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

fakeroot rootfs_rpm_do_rootfs () {
	#set +x

	${RPM_PREPROCESS_COMMANDS}

	#createrepo "${DEPLOY_DIR_RPM}"

	# install packages
	# This needs to work in the same way as populate_sdk_rpm.bbclass!
	export INSTALL_ROOTFS_RPM="${IMAGE_ROOTFS}"
	export INSTALL_PLATFORM_RPM="${TARGET_ARCH}"
	export INSTALL_CONFBASE_RPM="${RPMCONF_TARGET_BASE}"
	export INSTALL_PACKAGES_NORMAL_RPM="${PACKAGE_INSTALL}"
	export INSTALL_PACKAGES_ATTEMPTONLY_RPM="${PACKAGE_INSTALL_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_RPM="${LINGUAS_INSTALL}"
	export INSTALL_PROVIDENAME_RPM=""
	export INSTALL_TASK_RPM="rootfs_rpm_do_rootfs"

	# Setup base system configuration
	mkdir -p ${INSTALL_ROOTFS_RPM}/etc/rpm/

	mkdir -p ${INSTALL_ROOTFS_RPM}${rpmlibdir}
	mkdir -p ${INSTALL_ROOTFS_RPM}${rpmlibdir}/log
	cat > ${INSTALL_ROOTFS_RPM}${rpmlibdir}/DB_CONFIG << EOF
# ================ Environment
set_data_dir            .
set_create_dir          .
set_lg_dir              ./log
set_tmp_dir             ./tmp

# -- thread_count must be >= 8
set_thread_count        64

# ================ Logging

# ================ Memory Pool
set_mp_mmapsize         268435456

# ================ Locking
set_lk_max_locks        16384
set_lk_max_lockers      16384
set_lk_max_objects      16384
mutex_set_max           163840

# ================ Replication
EOF

	# List must be prefered to least preferred order
	INSTALL_PLATFORM_EXTRA_RPM=""
	for each_arch in ${MULTILIB_PACKAGE_ARCHS} ${PACKAGE_ARCHS}; do
		INSTALL_PLATFORM_EXTRA_RPM="$each_arch $INSTALL_PLATFORM_EXTRA_RPM"
	done
	export INSTALL_PLATFORM_RPM

	package_install_internal_rpm

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
	cat > ${IMAGE_ROOTFS}${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}configure << EOF
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
rm -f ${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}configure
EOF
	chmod 0755 ${IMAGE_ROOTFS}${sysconfdir}/rcS.d/S${POSTINSTALL_INITPOSITION}configure

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
		resolve_package_rpm foo ${RPMCONF_TARGET_BASE}.conf || true
	fi
}

remove_packaging_data_files() {
	rm -rf ${IMAGE_ROOTFS}${rpmlibdir}
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
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
			pkg_name=$(resolve_package_rpm $pkg-locale-$lang ${RPMCONF_TARGET_BASE}.conf)
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

    ml_package_archs = ""
    multilibs = d.getVar('MULTILIBS', True) or ""
    for ext in multilibs.split():
        eext = ext.split(':')
        if len(eext) > 1 and eext[0] == 'multilib':
            localdata = bb.data.createCopy(d)
            default_tune = localdata.getVar("DEFAULTTUNE_virtclass-multilib-" + eext[1], False)
            if default_tune:
                localdata.setVar("DEFAULTTUNE", default_tune)
            localdata.setVar("MACHINE_ARCH", eext[1] + "_" + localdata.getVar("MACHINE_ARCH", False))
            package_archs = localdata.getVar("PACKAGE_ARCHS", True) or ""
            ml_package_archs += " " + package_archs
            #bb.note("ML_PACKAGE_ARCHS %s %s %s" % (eext[1], localdata.getVar("PACKAGE_ARCHS", True) or "(none)", overrides))
    bb.data.setVar('MULTILIB_PACKAGE_ARCHS', ml_package_archs, d)
}
