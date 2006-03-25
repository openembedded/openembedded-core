PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
SECTION = "x11/libs"
DEPENDS = "libx11 fixesext"
DESCRIPTION = "X Fixes extension library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xfixes"
S = "${WORKDIR}/Xfixes"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
