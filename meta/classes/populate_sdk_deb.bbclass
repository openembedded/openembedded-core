do_populate_sdk[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot bzip2-native:do_populate_sysroot"

python () {
    # Map TARGET_ARCH to Debian's ideas about architectures
    darch = d.getVar('SDK_ARCH', True)
    if darch in ["x86", "i486", "i586", "i686", "pentium"]:
         d.setVar('DEB_SDK_ARCH', 'i386')
    elif darch == "x86_64":
         d.setVar('DEB_SDK_ARCH', 'amd64')
    elif darch == "arm":
         d.setVar('DEB_SDK_ARCH', 'armel')
}


do_populate_sdk[lockfiles] += "${DEPLOY_DIR_DEB}/deb.lock"

# This will of course only work after rootfs_deb_do_rootfs or populate_sdk_deb has been called
DPKG_QUERY_COMMAND = "${STAGING_BINDIR_NATIVE}/dpkg-query --admindir=$INSTALL_ROOTFS_DEB/var/lib/dpkg"
