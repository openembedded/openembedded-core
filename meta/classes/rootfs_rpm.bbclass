#
# Creates a root filesystem out of rpm packages
#

ROOTFS_PKGMANAGE = "rpm smartpm"
ROOTFS_PKGMANAGE_BOOTSTRAP = "run-postinsts"

# Add 50Meg of extra space for Smart
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@base_contains("PACKAGE_INSTALL", "smartpm", " + 51200", "" ,d)}"

# Smart is python based, so be sure python-native is available to us.
EXTRANATIVEPATH += "python-native"

do_rootfs[depends] += "rpm-native:do_populate_sysroot"
do_rootfs[depends] += "rpmresolve-native:do_populate_sysroot"
do_rootfs[depends] += "python-smartpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_rootfs[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
do_rootfs[depends] += "createrepo-native:do_populate_sysroot"

do_rootfs[recrdeptask] += "do_package_write_rpm"
rootfs_rpm_do_rootfs[vardepsexclude] += "BUILDNAME"

RPM_PREPROCESS_COMMANDS = "package_update_index_rpm; "
RPM_POSTPROCESS_COMMANDS = "rpm_setup_smart_target_config; "

rpmlibdir = "/var/lib/rpm"
opkglibdir = "${localstatedir}/lib/opkg"

RPMOPTS="--dbpath ${rpmlibdir}"
RPM="rpm ${RPMOPTS}"

# RPM doesn't work with multiple rootfs generation at once due to collisions in the use of files 
# in ${DEPLOY_DIR_RPM}. This can be removed if package_update_index_rpm can be called concurrently
do_rootfs[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

fakeroot rootfs_rpm_do_rootfs () {
	${RPM_PREPROCESS_COMMANDS}

	# install packages
	# This needs to work in the same way as populate_sdk_rpm.bbclass!
	export INSTALL_ROOTFS_RPM="${IMAGE_ROOTFS}"
	export INSTALL_PLATFORM_RPM="$(echo ${TARGET_ARCH} | tr - _)${TARGET_VENDOR}-${TARGET_OS}"
	export INSTALL_PACKAGES_RPM="${PACKAGE_INSTALL}"
	export INSTALL_PACKAGES_ATTEMPTONLY_RPM="${PACKAGE_INSTALL_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_RPM="${LINGUAS_INSTALL}"
	export INSTALL_PROVIDENAME_RPM=""
	export INSTALL_TASK_RPM="rootfs_rpm_do_rootfs"
	export INSTALL_COMPLEMENTARY_RPM=""

	# Setup base system configuration
	mkdir -p ${INSTALL_ROOTFS_RPM}/etc/rpm/

	# List must be prefered to least preferred order
	default_extra_rpm=""
	INSTALL_PLATFORM_EXTRA_RPM=""
	for os in ${MULTILIB_OS_LIST} ; do
		old_IFS="$IFS"
		IFS=":"
		set -- $os
		IFS="$old_IFS"
		mlib=$1
		mlib_os=$2
		for prefix in ${MULTILIB_PREFIX_LIST} ; do
			old_IFS="$IFS"
			IFS=":"
			set -- $prefix
			IFS="$old_IFS"
			if [ "$mlib" != "$1" ]; then
				continue
			fi
			shift #remove mlib
			while [ -n "$1" ]; do
				platform="$(echo $1 | tr - _)-.*-$mlib_os"
				if [ "$mlib" = "${BBEXTENDVARIANT}" ]; then
					default_extra_rpm="$default_extra_rpm $platform"
				else
					INSTALL_PLATFORM_EXTRA_RPM="$INSTALL_PLATFORM_EXTRA_RPM $platform"
				fi
				shift
			done
		done
	done
	if [ -n "$default_extra_rpm" ]; then
		INSTALL_PLATFORM_EXTRA_RPM="$default_extra_rpm $INSTALL_PLATFORM_EXTRA_RPM"
	fi
	export INSTALL_PLATFORM_EXTRA_RPM

	package_install_internal_rpm

	rootfs_install_complementary

	export D=${IMAGE_ROOTFS}
	export OFFLINE_ROOT=${IMAGE_ROOTFS}
	export IPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}
	export OPKG_OFFLINE_ROOT=${IMAGE_ROOTFS}

	${ROOTFS_POSTINSTALL_COMMAND}

	# Report delayed package scriptlets
	for i in ${IMAGE_ROOTFS}/etc/rpm-postinsts/*; do
		if [ -f $i ]; then
			echo "Delayed package scriptlet: `head -n 3 $i | tail -n 1`"
		fi
	done

	install -d ${IMAGE_ROOTFS}/${sysconfdir}
	echo ${BUILDNAME} > ${IMAGE_ROOTFS}/${sysconfdir}/version

	${RPM_POSTPROCESS_COMMANDS}
	${ROOTFS_POSTPROCESS_COMMAND}

	if ${@base_contains("IMAGE_FEATURES", "read-only-rootfs", "true", "false" ,d)}; then
		if [ -d ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts ] ; then
		        failed_pkgs=$(ls -A ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts)
			if [ -n "$failed_pkgs" ] ; then
				bberror "The following post-install scripts could not be run offline and rootfs is read-only: $failed_pkgs"
				exit 1
			fi
		fi
	fi

	rm -rf ${IMAGE_ROOTFS}/var/cache2/
	rm -rf ${IMAGE_ROOTFS}/var/run2/
	rm -rf ${IMAGE_ROOTFS}/var/log2/

	# remove lock files
	rm -f ${IMAGE_ROOTFS}${rpmlibdir}/__db.*

	# Remove all remaining resolver files
	rm -rf ${IMAGE_ROOTFS}/install

	log_check rootfs
}

rootfs_rpm_do_rootfs[vardeps] += "delayed_postinsts"

delayed_postinsts() {
	if [ -d ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts ]; then
		ls ${IMAGE_ROOTFS}${sysconfdir}/rpm-postinsts
	fi
}

save_postinsts() {
	# this is just a stub. For RPM, the failed postinstalls are already saved in
	# /etc/rpm-postinsts
	true
}

remove_packaging_data_files() {
	# Save the rpmlib for increment rpm image generation
	t="${T}/saved_rpmlib/var/lib"
	rm -fr $t
	mkdir -p $t
	mv ${IMAGE_ROOTFS}${rpmlibdir} $t
	rm -rf ${IMAGE_ROOTFS}${opkglibdir}
	rm -rf ${IMAGE_ROOTFS}/var/lib/smart
}

rpm_setup_smart_target_config() {
	# Set up smart configuration for the target
	rm -rf ${IMAGE_ROOTFS}/var/lib/smart
	smart --data-dir=${IMAGE_ROOTFS}/var/lib/smart channel --add rpmsys type=rpm-sys -y
	package_write_smart_config ${IMAGE_ROOTFS}
	rm -f ${IMAGE_ROOTFS}/var/lib/smart/config.old
}

rootfs_install_packages() {
	# Note - we expect the variables not set here to already have been set
	export INSTALL_PACKAGES_RPM=""
	export INSTALL_PACKAGES_ATTEMPTONLY_RPM="`cat $1`"
	export INSTALL_PROVIDENAME_RPM=""
	export INSTALL_TASK_RPM="rootfs_install_packages"
	export INSTALL_COMPLEMENTARY_RPM="1"

	package_install_internal_rpm
}

rootfs_uninstall_packages() {
	rpm -e --nodeps --root=${IMAGE_ROOTFS} --dbpath=/var/lib/rpm\
		--define='_cross_scriptlet_wrapper ${WORKDIR}/scriptlet_wrapper'\
		--define='_tmppath /install/tmp' $@

	# remove temp directory
	rm -rf ${IMAGE_ROOTFS}/install
}

python () {
    if d.getVar('BUILD_IMAGES_FROM_FEEDS', True):
        flags = d.getVarFlag('do_rootfs', 'recrdeptask')
        flags = flags.replace("do_package_write_rpm", "")
        flags = flags.replace("do_deploy", "")
        flags = flags.replace("do_populate_sysroot", "")
        d.setVarFlag('do_rootfs', 'recrdeptask', flags)
        d.setVar('RPM_PREPROCESS_COMMANDS', '')
        d.setVar('RPM_POSTPROCESS_COMMANDS', '')

    # The following code should be kept in sync w/ the populate_sdk_rpm version.

    # package_arch order is reversed.  This ensures the -best- match is listed first!
    package_archs = d.getVar("PACKAGE_ARCHS", True) or ""
    package_archs = ":".join(package_archs.split()[::-1])
    package_os = d.getVar("TARGET_OS", True) or ""
    ml_prefix_list = "%s:%s" % ('default', package_archs)
    ml_os_list = "%s:%s" % ('default', package_os)
    multilibs = d.getVar('MULTILIBS', True) or ""
    for ext in multilibs.split():
        eext = ext.split(':')
        if len(eext) > 1 and eext[0] == 'multilib':
            localdata = bb.data.createCopy(d)
            default_tune = localdata.getVar("DEFAULTTUNE_virtclass-multilib-" + eext[1], False)
            if default_tune:
                localdata.setVar("DEFAULTTUNE", default_tune)
                bb.data.update_data(localdata)
            package_archs = localdata.getVar("PACKAGE_ARCHS", True) or ""
            package_archs = ":".join([i in "all noarch any".split() and i or eext[1]+"_"+i for i in package_archs.split()][::-1])
            package_os = localdata.getVar("TARGET_OS", True) or ""
            ml_prefix_list += " %s:%s" % (eext[1], package_archs)
            ml_os_list += " %s:%s" % (eext[1], package_os)
    d.setVar('MULTILIB_PREFIX_LIST', ml_prefix_list)
    d.setVar('MULTILIB_OS_LIST', ml_os_list)
}
