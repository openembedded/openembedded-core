require gcc-cross_${PV}.bb
require gcc-cross-intermediate.inc
PR = "r1"

# Hack till we fix *libc properly
do_install_append() {
        ln -sf ${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include-fixed/* ${D}${CROSS_DIR}/lib/gcc/${TARGET_SYS}/${BINV}/include/
}

