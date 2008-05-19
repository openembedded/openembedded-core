require gcc-configure-common.inc

EXTRA_OECONF_PATHS = " \
    --with-local-prefix=${STAGING_DIR_TARGET}${layout_prefix} \
    --with-gxx-include-dir=${includedir}/c++/${BINV}/"

