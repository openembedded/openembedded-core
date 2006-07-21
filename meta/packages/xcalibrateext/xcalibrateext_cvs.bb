PV = "0.0+cvs${SRCDATE}"
SECTION = "x11/libs"
LICENSE = "BSD-X"
DESCRIPTION = "XCalibrate extension headers"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=XCalibrateExt"
S = "${WORKDIR}/XCalibrateExt"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
