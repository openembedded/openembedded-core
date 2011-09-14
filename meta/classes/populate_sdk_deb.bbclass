do_populate_sdk[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot bzip2-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_deb"


DEB_SDK_ARCH = "${@[bb.data.getVar('SDK_ARCH', d, 1), "i386"]\
                [bb.data.getVar('SDK_ARCH', d, 1) in \
                ["x86", "i486", "i586", "i686", "pentium"]]}"

populate_sdk_post_deb () {

	local target_rootfs=$1

	tar -cf - -C ${STAGING_ETCDIR_NATIVE} -ps apt | tar -xf - -C ${target_rootfs}/etc
}

fakeroot populate_sdk_deb () {

	# update index
	package_update_index_deb

	## install target ##
	# This needs to work in the same way as rootfs_deb.bbclass
	echo "Installing TARGET packages"

	mkdir -p ${IMAGE_ROOTFS}/var/dpkg/alternatives

	export INSTALL_ROOTFS_DEB="${SDK_OUTPUT}/${SDKTARGETSYSROOT}"
	export INSTALL_BASEARCH_DEB="${DPKG_ARCH}"
	export INSTALL_ARCHS_DEB="${PACKAGE_ARCHS}"
	export INSTALL_PACKAGES_NORMAL_DEB="${TOOLCHAIN_TARGET_TASK}"
	export INSTALL_PACKAGES_ATTEMPTONLY_DEB=""
	export PACKAGES_LINGUAS_DEB=""
	export INSTALL_TASK_DEB="populate_sdk-target"

	package_install_internal_deb

	populate_sdk_post_deb ${INSTALL_ROOTFS_DEB}

	populate_sdk_log_check populate_sdk

	## install nativesdk ##
	echo "Installing NATIVESDK packages"
	export INSTALL_ROOTFS_DEB="${SDK_OUTPUT}"
	export INSTALL_BASEARCH_DEB="${DEB_SDK_ARCH}"
	export INSTALL_ARCHS_DEB="${SDK_PACKAGE_ARCHS}"
	export INSTALL_PACKAGES_NORMAL_DEB="${TOOLCHAIN_HOST_TASK}"
	export INSTALL_PACKAGES_ATTEMPTONLY_DEB=""
	export PACKAGES_LINGUAS_DEB=""
	export INSTALL_TASK_DEB="populate_sdk-nativesdk"

	package_install_internal_deb
	populate_sdk_post_deb ${SDK_OUTPUT}/${SDKPATHNATIVE}

	#move remainings
	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}/var/dpkg
	mv ${SDK_OUTPUT}/var/dpkg/* ${SDK_OUTPUT}/${SDKPATHNATIVE}/var/dpkg
	rm -rf ${SDK_OUTPUT}/var

	populate_sdk_log_check populate_sdk
}

