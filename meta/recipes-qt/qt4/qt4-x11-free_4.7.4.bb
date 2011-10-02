require qt4-x11-free.inc
require qt-${PV}.inc

PR = "${INC_PR}.3"

QT_CONFIG_FLAGS_append_armv6-vfp = " -no-neon "

QT_CONFIG_FLAGS += " \
 -no-embedded \
 -xrandr \
 -x11"

DEFAULT_PREFERENCE = "-1"
