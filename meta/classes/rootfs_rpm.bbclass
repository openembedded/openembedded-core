#
# Creates a root filesystem out of rpm packages
#

ROOTFS_PKGMANAGE = "rpm smartpm"
ROOTFS_PKGMANAGE_BOOTSTRAP = "run-postinsts"

# Add 50Meg of extra space for Smart
IMAGE_ROOTFS_EXTRA_SPACE_append = "${@base_contains("PACKAGE_INSTALL", "smartpm", " + 51200", "" ,d)}"

# Smart is python based, so be sure python-native is available to us.
EXTRANATIVEPATH += "python-native"

do_rootfs[depends] += "rpm-native:do_populate_sysroot"
do_rootfs[depends] += "rpmresolve-native:do_populate_sysroot"
do_rootfs[depends] += "python-smartpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_rootfs[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
do_rootfs[depends] += "createrepo-native:do_populate_sysroot"

do_rootfs[recrdeptask] += "do_package_write_rpm"
rootfs_rpm_do_rootfs[vardepsexclude] += "BUILDNAME"
do_rootfs[vardeps] += "PACKAGE_FEED_URIS"

# RPM doesn't work with multiple rootfs generation at once due to collisions in the use of files 
# in ${DEPLOY_DIR_RPM}. This can be removed if package_update_index_rpm can be called concurrently
do_rootfs[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"

python () {
    if d.getVar('BUILD_IMAGES_FROM_FEEDS', True):
        flags = d.getVarFlag('do_rootfs', 'recrdeptask')
        flags = flags.replace("do_package_write_rpm", "")
        flags = flags.replace("do_deploy", "")
        flags = flags.replace("do_populate_sysroot", "")
        d.setVarFlag('do_rootfs', 'recrdeptask', flags)
        d.setVar('RPM_PREPROCESS_COMMANDS', '')
        d.setVar('RPM_POSTPROCESS_COMMANDS', '')

}
