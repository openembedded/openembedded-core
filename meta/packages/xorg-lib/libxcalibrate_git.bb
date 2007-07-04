require xorg-lib-common.inc

DESCRIPTION = "	Touchscreen calibration client library"
LICENSE = "BSD-X"
DEPENDS = "virtual/libx11 calibrateproto libxext"
PV = "0.0+git${SRCDATE}"
PR = "r2"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libXCalibrate;protocol=git;tag=4be232e30cd33a44a1ce6d3ec429ee6101540c62 \
           file://coords.patch;patch=1"

S = "${WORKDIR}/git"

FILES_${PN}-locale += "${datadir}/X11/locale"
