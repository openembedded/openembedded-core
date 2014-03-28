do_populate_sdk[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot bzip2-native:do_populate_sysroot"

DEB_SDK_ARCH = "${@[d.getVar('SDK_ARCH', True), "i386"]\
                [d.getVar('SDK_ARCH', True) in \
                ["x86", "i486", "i586", "i686", "pentium"]]}"

DEB_SDK_ARCH = "${@[d.getVar('SDK_ARCH', True), "amd64"]\
                [d.getVar('SDK_ARCH', True) == "x86_64"]}"

do_populate_sdk[lockfiles] += "${DEPLOY_DIR_DEB}/deb.lock"

# This will of course only work after rootfs_deb_do_rootfs or populate_sdk_deb has been called
DPKG_QUERY_COMMAND = "${STAGING_BINDIR_NATIVE}/dpkg-query --admindir=$INSTALL_ROOTFS_DEB/var/lib/dpkg"
