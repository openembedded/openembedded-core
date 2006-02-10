PV = "0.0+cvs${SRCDATE}"
LICENSE = "BSD-X"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "x11 xcalibrateext xext"
DESCRIPTION = "XCalibrate client-side library"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=XCalibrate"
S = "${WORKDIR}/XCalibrate"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
