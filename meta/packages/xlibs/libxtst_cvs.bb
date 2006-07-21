LICENSE = "GPL"
PV = "0.0+cvs${SRCDATE}"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "libx11 recordext libxext"
PROVIDES = "xtst"
DESCRIPTION = "X Test Extension: client side library"
PR = "r3"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xtst"
S = "${WORKDIR}/Xtst"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
