require recipes-devtools/gcc/gcc-${PV}.inc
require gcc-runtime.inc

# Disable ifuncs for libatomic on arm conflicts -march/-mcpu
EXTRA_OECONF_append_arm = " libat_cv_have_ifunc=no "

FILES_libgomp-dev += "\
    ${libdir}/gcc/${TARGET_SYS}/${BINV}/include/openacc.h \
"

