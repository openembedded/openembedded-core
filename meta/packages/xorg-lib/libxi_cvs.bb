PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT-X"
SECTION = "x11/libs"
DEPENDS = "xproto virtual/libx11 xextensions"
DESCRIPTION = "X Input extension library."
PR = "r1"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xi \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/Xi"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
