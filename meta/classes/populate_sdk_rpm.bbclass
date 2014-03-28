# Smart is python based, so be sure python-native is available to us.
EXTRANATIVEPATH += "python-native"

do_populate_sdk[depends] += "rpm-native:do_populate_sysroot"
do_populate_sdk[depends] += "rpmresolve-native:do_populate_sysroot"
do_populate_sdk[depends] += "python-smartpm-native:do_populate_sysroot"

# Needed for update-alternatives
do_populate_sdk[depends] += "opkg-native:do_populate_sysroot"

# Creating the repo info in do_rootfs
do_populate_sdk[depends] += "createrepo-native:do_populate_sysroot"

rpmlibdir = "/var/lib/rpm"

do_populate_sdk[lockfiles] += "${DEPLOY_DIR_RPM}/rpm.lock"
