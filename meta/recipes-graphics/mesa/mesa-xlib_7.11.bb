include mesa-common.inc
include mesa-${PV}.inc

PR = "${INC_PR}.0"

EXTRA_OECONF += "--with-driver=xlib --without-gallium-drivers"
