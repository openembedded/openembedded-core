do_populate_sdk[depends] += "opkg-native:do_populate_sysroot opkg-utils-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_ipk"

do_populate_sdk[lockfiles] += "${WORKDIR}/ipk.lock"

rootfs_list_installed_depends() {
	opkg-cl ${OPKG_ARGS} status | opkg-query-helper.py
}
