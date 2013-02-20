# Smart is python based, so be sure python-native is available to us.
EXTRANATIVEPATH += "python-native"

do_populate_sdk[depends] += "rpm-native:do_populate_sysroot"
do_populate_sdk[depends] += "rpmresolve-native:do_populate_sysroot"
do_populate_sdk[depends] += "python-smartpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_populate_sdk[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
do_populate_sdk[depends] += "createrepo-native:do_populate_sysroot"

do_populate_sdk[recrdeptask] += "do_package_write_rpm"

rpmlibdir = "/var/lib/rpm"
RPMOPTS="--dbpath ${rpmlibdir}"
RPM="rpm ${RPMOPTS}"

do_populate_sdk[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

populate_sdk_post_rpm () {

	local target_rootfs=$1

	# remove lock files
	rm -f ${target_rootfs}/__db.*

	# Remove all remaining resolver files
	rm -rf ${target_rootfs}/install
	rm -rf ${target_rootfs}/var/lib/smart
}

populate_sdk_rpm () {

	package_update_index_rpm

	## install target ##
	# This needs to work in the same way as rootfs_rpm.bbclass!
	#
	export INSTALL_ROOTFS_RPM="${SDK_OUTPUT}/${SDKTARGETSYSROOT}"
	export INSTALL_PLATFORM_RPM="${TARGET_ARCH}"
	export INSTALL_PACKAGES_RPM="${TOOLCHAIN_TARGET_TASK}"
	export INSTALL_PACKAGES_ATTEMPTONLY_RPM="${TOOLCHAIN_TARGET_TASK_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_RPM=""
	# We don't need any of these runtime items for the SDK, so
	# just make the system assume they exist.
	export INSTALL_PROVIDENAME_RPM="/bin/sh /bin/bash /usr/bin/env /usr/bin/perl pkgconfig"
	export INSTALL_TASK_RPM="populate_sdk-target"
	export INSTALL_COMPLEMENTARY_RPM=""
	export INTERCEPT_DIR=${WORKDIR}/intercept_scripts
	export NATIVE_ROOT=${STAGING_DIR_NATIVE}

	# Setup base system configuration
	mkdir -p ${INSTALL_ROOTFS_RPM}/etc/rpm/

	# List must be prefered to least preferred order
	default_extra_rpm=""
	INSTALL_PLATFORM_EXTRA_RPM=""
	for i in ${MULTILIB_PREFIX_LIST} ; do
		old_IFS="$IFS"
		IFS=":"
		set $i
		IFS="$old_IFS"
		mlib=$1
		shift #remove mlib
		while [ -n "$1" ]; do
			if [ "$mlib" = "${BBEXTENDVARIANT}" ]; then
				default_extra_rpm="$default_extra_rpm $1"
			else
				INSTALL_PLATFORM_EXTRA_RPM="$INSTALL_PLATFORM_EXTRA_RPM $1"
			fi
			shift
		done
	done
	if [ -n "$default_extra_rpm" ]; then
		INSTALL_PLATFORM_EXTRA_RPM="$default_extra_rpm $INSTALL_PLATFORM_EXTRA_RPM"
	fi
	export INSTALL_PLATFORM_EXTRA_RPM

	package_install_internal_rpm
	${POPULATE_SDK_POST_TARGET_COMMAND}
	populate_sdk_post_rpm ${INSTALL_ROOTFS_RPM}

	## install nativesdk ##
	echo "Installing NATIVESDK packages"
	export INSTALL_ROOTFS_RPM="${SDK_OUTPUT}"
	export INSTALL_PLATFORM_RPM="${SDK_ARCH}"
	export INSTALL_PACKAGES_RPM="${TOOLCHAIN_HOST_TASK}"
	export INSTALL_PACKAGES_ATTEMPTONLY_RPM="${TOOLCHAIN_TARGET_HOST_ATTEMPTONLY}"
	export INSTALL_PACKAGES_LINGUAS_RPM=""
	export INSTALL_PROVIDENAME_RPM="/bin/sh /bin/bash /usr/bin/env /usr/bin/perl pkgconfig libGL.so()(64bit) libGL.so"
	export INSTALL_TASK_RPM="populate_sdk_rpm-nativesdk"
	export INSTALL_COMPLEMENTARY_RPM=""

	# List must be prefered to least preferred order
	INSTALL_PLATFORM_EXTRA_RPM=""
	for each_arch in ${SDK_PACKAGE_ARCHS} ; do
		INSTALL_PLATFORM_EXTRA_RPM="$each_arch $INSTALL_PLATFORM_EXTRA_RPM"
	done
	export INSTALL_PLATFORM_EXTRA_RPM

	package_install_internal_rpm --sdk
	populate_sdk_post_rpm ${INSTALL_ROOTFS_RPM}

	# move host RPM library data
	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/rpm
	mv ${SDK_OUTPUT}${rpmlibdir}/* ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/rpm/
	rm -Rf ${SDK_OUTPUT}/var

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}
	mv ${SDK_OUTPUT}/etc/* ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}/
	rm -rf ${SDK_OUTPUT}/etc

	populate_sdk_log_check populate_sdk
}

python () {
    # The following code should be kept in sync w/ the rootfs_rpm version.

    # package_arch order is reversed.  This ensures the -best- match is listed first!
    package_archs = d.getVar("PACKAGE_ARCHS", True) or ""
    package_archs = ":".join(package_archs.split()[::-1])
    ml_prefix_list = "%s:%s" % ('default', package_archs)
    multilibs = d.getVar('MULTILIBS', True) or ""
    for ext in multilibs.split():
        eext = ext.split(':')
        if len(eext) > 1 and eext[0] == 'multilib':
            localdata = bb.data.createCopy(d)
            default_tune = localdata.getVar("DEFAULTTUNE_virtclass-multilib-" + eext[1], False)
            if default_tune:
                localdata.setVar("DEFAULTTUNE", default_tune)
            package_archs = localdata.getVar("PACKAGE_ARCHS", True) or ""
            package_archs = ":".join([i in "all noarch any".split() and i or eext[1]+"_"+i for i in package_archs.split()][::-1])
            ml_prefix_list += " %s:%s" % (eext[1], package_archs)
    d.setVar('MULTILIB_PREFIX_LIST', ml_prefix_list)
}

