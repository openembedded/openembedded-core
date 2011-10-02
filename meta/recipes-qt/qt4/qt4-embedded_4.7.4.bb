require qt-${PV}.inc
require qt4-embedded.inc

PR = "${INC_PR}.3"

QT_CONFIG_FLAGS_append_armv6-vfp = " -no-neon "

QT_CONFIG_FLAGS += " \
 -exceptions \
"

DEFAULT_PREFERENCE = "-1"
