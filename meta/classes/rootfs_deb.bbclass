#
# Copyright 2006-2007 Openedhand Ltd.
#

ROOTFS_PKGMANAGE = "dpkg apt"
ROOTFS_PKGMANAGE_BOOTSTRAP  = "run-postinsts"

do_rootfs[depends] += "dpkg-native:do_populate_sysroot apt-native:do_populate_sysroot"
do_rootfs[recrdeptask] += "do_package_write_deb"
rootfs_deb_do_rootfs[vardepsexclude] += "BUILDNAME"
do_rootfs[vardeps] += "PACKAGE_FEED_URIS"

do_rootfs[lockfiles] += "${DEPLOY_DIR_DEB}/deb.lock"

python rootfs_deb_bad_recommendations() {
    if d.getVar("BAD_RECOMMENDATIONS", True):
        bb.warn("Debian package install does not support BAD_RECOMMENDATIONS")
}
do_rootfs[prefuncs] += "rootfs_deb_bad_recommendations"

DEB_POSTPROCESS_COMMANDS = ""

opkglibdir = "${localstatedir}/lib/opkg"

