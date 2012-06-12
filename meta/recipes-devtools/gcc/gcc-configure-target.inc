require gcc-configure-common.inc

EXTRA_OECONF_PATHS = " \
    --with-sysroot=\
    --with-build-sysroot=${STAGING_DIR_TARGET} \
    --with-gxx-include-dir=${includedir}/c++/"
