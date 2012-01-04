do_populate_sdk[depends] += "opkg-native:do_populate_sysroot opkg-utils-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_ipk"

populate_sdk_ipk() {

	rm -f ${IPKGCONF_TARGET}
	touch ${IPKGCONF_TARGET}
	rm -f ${IPKGCONF_SDK}
	touch ${IPKGCONF_SDK}

	package_update_index_ipk
	package_generate_ipkg_conf

	export INSTALL_PACKAGES_ATTEMPTONLY_IPK=""
	export INSTALL_PACKAGES_LINGUAS_IPK=""
	export INSTALL_TASK_IPK="populate_sdk"

	#install target
	export INSTALL_ROOTFS_IPK="${SDK_OUTPUT}/${SDKTARGETSYSROOT}"
	export INSTALL_CONF_IPK="${IPKGCONF_TARGET}"
	export INSTALL_PACKAGES_IPK="${TOOLCHAIN_TARGET_TASK}"

	export D=${INSTALL_ROOTFS_IPK}
	export OFFLINE_ROOT=${INSTALL_ROOTFS_IPK}
	export IPKG_OFFLINE_ROOT=${INSTALL_ROOTFS_IPK}
	export OPKG_OFFLINE_ROOT=${IPKG_OFFLINE_ROOT}

	package_install_internal_ipk

	#install host
	export INSTALL_ROOTFS_IPK="${SDK_OUTPUT}"
	export INSTALL_CONF_IPK="${IPKGCONF_SDK}"
	export INSTALL_PACKAGES_IPK="${TOOLCHAIN_HOST_TASK}"

	package_install_internal_ipk

	#post clean up
	install -d ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/${sysconfdir}
	install -m 0644 ${IPKGCONF_TARGET} ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKTARGETSYSROOT}/${sysconfdir}/

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}
	install -m 0644 ${IPKGCONF_SDK} ${SDK_OUTPUT}/${SDKPATHNATIVE}/${sysconfdir}/

	install -d ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/opkg
	mv ${SDK_OUTPUT}/var/lib/opkg/* ${SDK_OUTPUT}/${SDKPATHNATIVE}${localstatedir_nativesdk}/lib/opkg/
	rm -Rf ${SDK_OUTPUT}/var

	populate_sdk_log_check populate_sdk
}
