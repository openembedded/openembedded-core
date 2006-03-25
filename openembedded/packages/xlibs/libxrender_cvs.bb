PV = "0.0+cvs${SRCDATE}"
PR = "r1"
SECTION = "libs"
DEPENDS = "renderext libx11"
DESCRIPTION = "X Render extension library."
LICENSE = "BSD"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xrender"
S = "${WORKDIR}/Xrender"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
