require gcc-cross_${PV}.bb
require gcc-cross-initial.inc

PR = "r0"

# Hack till we fix *libc properly
do_install_append() {
        ln -sf ${STAGING_DIR_NATIVE}${prefix_native}/lib/gcc/${TARGET_SYS}/${BINV}/include-fixed/* ${D}${STAGING_DIR_NATIVE}${prefix_native}/lib/gcc/${TARGET_SYS}/${BINV}/include/
}

