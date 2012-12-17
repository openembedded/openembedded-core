GCCMULTILIB = "--enable-multilib"
require gcc-configure-common.inc

EXTRA_OECONF_PATHS = " \
    --with-sysroot=/ \
    --with-build-sysroot=${STAGING_DIR_TARGET} \
    --with-native-system-header-dir=${STAGING_DIR_TARGET}${target_includedir} \
    --with-gxx-include-dir=${includedir}/c++/"
