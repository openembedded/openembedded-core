require libgcc-common.inc

DEPENDS = "virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++"

PACKAGES = "\
    ${PN} \
    ${PN}-dev \
    ${PN}-dbg \
    libgcov-dev \
"

FILES_${PN} = "${base_libdir}/libgcc*.so.*"
FILES_${PN}-dev = "\
    ${base_libdir}/libgcc*.so \
    ${libdir}/${TARGET_SYS}/${BINV}/*crt* \
    ${libdir}/${TARGET_SYS}/${BINV}/64 \
    ${libdir}/${TARGET_SYS}/${BINV}/32 \
    ${libdir}/${TARGET_SYS}/${BINV}/x32 \
    ${libdir}/${TARGET_SYS}/${BINV}/n32 \
    ${libdir}/${TARGET_SYS}/${BINV}/libgcc* \
    ${@base_conditional('TARGET_SYS_NO_EXTENSION', '${TARGET_SYS}', '', '${libdir}/${TARGET_SYS_NO_EXTENSION}', d)} \
"
FILES_libgcov-dev = "\
    ${libdir}/${TARGET_SYS}/${BINV}/libgcov.a \
"
FILES_${PN}-dbg += "${base_libdir}/.debug/"

LIBGCCBUILDTREENAME = "gcc-build-internal-"

do_package[depends] += "virtual/${MLPREFIX}libc:do_packagedata"
do_package_write_ipk[depends] += "virtual/${MLPREFIX}libc:do_packagedata"
do_package_write_deb[depends] += "virtual/${MLPREFIX}libc:do_packagedata"
do_package_write_rpm[depends] += "virtual/${MLPREFIX}libc:do_packagedata"

INSANE_SKIP_${PN}-dev = "staticdev"
INSANE_SKIP_${MLPREFIX}libgcov-dev = "staticdev"

