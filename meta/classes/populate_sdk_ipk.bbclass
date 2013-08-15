do_populate_sdk[depends] += "opkg-native:do_populate_sysroot opkg-utils-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_ipk"

do_populate_sdk[lockfiles] += "${WORKDIR}/ipk.lock"

populate_sdk_ipk() {

	rm -f ${IPKGCONF_TARGET}
	touch ${IPKGCONF_TARGET}
	rm -f ${IPKGCONF_SDK}
	touch ${IPKGCONF_SDK}

	package_update_index_ipk
	package_generate_ipkg_conf

	export INSTALL_PACKAGES_LINGUAS_IPK=""
	export INSTALL_TASK_IPK="populate_sdk"

	#install target
	export INSTALL_ROOTFS_IPK="${SDK_OUTPUT}/${SDKTARGETSYSROOT}"
	export INSTALL_CONF_IPK="${IPKGCONF_TARGET}"
	export INSTALL_PACKAGES_IPK="${TOOLCHAIN_TARGET_TASK}"
	export INSTALL_PACKAGES_ATTEMPTONLY_IPK="${TOOLCHAIN_TARGET_TASK_ATTEMPTONLY}"

	export D=${INSTALL_ROOTFS_IPK}
	export OFFLINE_ROOT=${INSTALL_ROOTFS_IPK}
	export IPKG_OFFLINE_ROOT=${INSTALL_ROOTFS_IPK}
	export OPKG_OFFLINE_ROOT=${IPKG_OFFLINE_ROOT}
	export INTERCEPT_DIR=${WORKDIR}/intercept_scripts
	export NATIVE_ROOT=${STAGING_DIR_NATIVE}

	package_install_internal_ipk

	${POPULATE_SDK_POST_TARGET_COMMAND}

	#install host
	export INSTALL_ROOTFS_IPK="${SDK_OUTPUT}"
	export INSTALL_CONF_IPK="${IPKGCONF_SDK}"
	export INSTALL_PACKAGES_IPK="${TOOLCHAIN_HOST_TASK}"
	export INSTALL_PACKAGES_ATTEMPTONLY_IPK="${TOOLCHAIN_HOST_TASK_ATTEMPTONLY}"

	package_install_internal_ipk

	${POPULATE_SDK_POST_HOST_COMMAND}

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

list_installed_packages() {
	if [ "$1" = "arch" ] ; then
		opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py -a
	elif [ "$1" = "file" ] ; then
		opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py -f | while read pkg pkgfile pkgarch
		do
			fullpath=`find ${DEPLOY_DIR_IPK} -name "$pkgfile" || true`
			if [ "$fullpath" = "" ] ; then
				echo "$pkg $pkgfile $pkgarch"
			else
				echo "$pkg $fullpath $pkgarch"
			fi
		done
	else
		opkg-cl ${OPKG_ARGS} list_installed | awk '{ print $1 }'
	fi
}

rootfs_list_installed_depends() {
	opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py
}
