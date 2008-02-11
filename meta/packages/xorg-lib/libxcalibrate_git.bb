require xorg-lib-common.inc

DESCRIPTION = "	Touchscreen calibration client library"
LICENSE = "BSD-X"
DEPENDS = "virtual/libx11 calibrateproto libxext"
PV = "0.0+git${SRCREV}"
PR = "r3"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libXCalibrate;protocol=git;tag=${SRCREV} \
           file://coords.patch;patch=1"

S = "${WORKDIR}/git"

FILES_${PN}-locale += "${datadir}/X11/locale"
