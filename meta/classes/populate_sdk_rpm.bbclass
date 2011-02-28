do_populate_sdk[depends] += "rpm-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_rpm"

rpmlibdir = "/var/lib/rpm"
RPMOPTS="--dbpath ${rpmlibdir} --define='_openall_before_chroot 1'"
RPM="rpm ${RPMOPTS}"

do_populate_sdk[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

populate_sdk_post_rpm () {

	local target_rootfs=$1

	# remove lock files
	rm -f ${target_rootfs}/__db.*

	# Move manifests into the directory with the logs
	mv ${target_rootfs}/install/*.manifest ${T}/

	# Remove all remaining resolver files
	rm -rf ${target_rootfs}/install
}

fakeroot populate_sdk_rpm () {

	package_update_index_rpm
	package_generate_rpm_conf

	export INSTALL_PACKAGES_ATTEMPTONLY_RPM=""
	export INSTALL_PACKAGES_LINGUAS_RPM=""
	export INSTALL_PROVIDENAME_RPM="/bin/sh"
	export INSTALL_TASK_RPM="populate_sdk"


	#install target
	export INSTALL_ROOTFS_RPM="${SDK_OUTPUT}/${SDKTARGETSYSROOT}"
	export INSTALL_PLATFORM_RPM="${TARGET_ARCHS}"
	export INSTALL_CONFBASE_RPM="${RPMCONF_TARGET_BASE}"
	export INSTALL_PACKAGES_NORMAL_RPM="${TOOLCHAIN_TARGET_TASK}"

	# List must be prefered to least preferred order
	INSTALL_PLATFORM_RPM=""
	for each_arch in ${PACKAGE_ARCHS} ; do
		INSTALL_PLATFORM_RPM="$each_arch $INSTALL_PLATFORM_RPM"
	done
	export INSTALL_PLATFORM_RPM

	package_install_internal_rpm
	populate_sdk_post_rpm ${INSTALL_ROOTFS_RPM}

	#install host
	export INSTALL_ROOTFS_RPM="${SDK_OUTPUT}"
	export INSTALL_PLATFORM_RPM="${SDK_ARCH}"
	export INSTALL_CONFBASE_RPM="${RPMCONF_HOST_BASE}"
	export INSTALL_PACKAGES_NORMAL_RPM="${TOOLCHAIN_HOST_TASK}"
	export INSTALL_PLATFORM_EXTRA_RPM=""
	for arch in ${PACKAGE_ARCHS}; do
		sdkarch=`echo $arch | sed -e 's/${HOST_ARCH}/${SDK_ARCH}/'`
		extension="-nativesdk"
		if [ "$sdkarch" = "all" -o "$sdkarch" = "any" -o "$sdkarch" = "noarch" ]; then
		    extension=""
		fi
		if [ -e ${DEPLOY_DIR_RPM}/$sdkarch$extension ]; then
			INSTALL_PLATFORM_EXTRA_RPM="$sdkarch $INSTALL_PLATFORM_EXTRA_RPM"
		fi
	done
	export INSTALL_PLATFORM_EXTRA_RPM

	package_install_internal_rpm
	populate_sdk_post_rpm ${INSTALL_ROOTFS_RPM}

	# move host RPM library data
	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/rpm
	mv ${SDK_OUTPUT}${rpmlibdir}/* ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/rpm/
	rm -Rf ${SDK_OUTPUT}/var

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}
	mv ${SDK_OUTPUT}/etc/* ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}/
	rm -rf ${SDK_OUTPUT}/etc

	populate_sdk_log_check populate_sdk

	# Workaround so the parser knows we need the resolve_package function!
	if false ; then
		resolve_package_rpm foo ${RPMCONF_TARGET_BASE}.conf || true
	fi
}
