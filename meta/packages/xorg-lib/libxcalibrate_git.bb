DESCRIPTION = "	Touchscreen calibration client library"
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"

PV = "0.0+git${SRCDATE}"

DEPENDS = "virtual/libx11 calibrateproto libxext"

FILES_${PN}-locale += "${datadir}/X11/locale"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libXCalibrate;protocol=git"
S = "${WORKDIR}/git"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
