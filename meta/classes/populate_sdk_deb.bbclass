do_populate_sdk[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot bzip2-native:do_populate_sysroot"
do_populate_sdk[recrdeptask] += "do_package_write_deb"


DEB_SDK_ARCH = "${@[d.getVar('SDK_ARCH', True), "i386"]\
                [d.getVar('SDK_ARCH', True) in \
                ["x86", "i486", "i586", "i686", "pentium"]]}"

do_populate_sdk[lockfiles] += "${DEPLOY_DIR_DEB}/deb.lock"

# This will of course only work after rootfs_deb_do_rootfs or populate_sdk_deb has been called
DPKG_QUERY_COMMAND = "${STAGING_BINDIR_NATIVE}/dpkg-query --admindir=$INSTALL_ROOTFS_DEB/var/lib/dpkg"

rootfs_list_installed_depends() {
	# Cheat here a little bit by using the opkg query helper util
	${DPKG_QUERY_COMMAND} -W -f='Package: ${Package}\nDepends: ${Depends}\nRecommends: ${Recommends}\n\n' | opkg-query-helper.py
}
