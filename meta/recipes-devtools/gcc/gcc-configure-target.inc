require gcc-configure-common.inc

EXTRA_OECONF_PATHS = " \
    --with-gxx-include-dir=${includedir}/c++/"
