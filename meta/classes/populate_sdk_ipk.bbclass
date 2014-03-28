do_populate_sdk[depends] += "opkg-native:do_populate_sysroot opkg-utils-native:do_populate_sysroot"

do_populate_sdk[lockfiles] += "${WORKDIR}/ipk.lock"
