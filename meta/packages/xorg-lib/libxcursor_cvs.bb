PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
SECTION = "x11/libs"
PRIORITY = "optional"
DESCRIPTION = "X Cursor library"
DEPENDS = "libxfixes"
PR = "r2"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xcursor"
S = "${WORKDIR}/Xcursor"
FILES_${PN} += "${libdir}/libXcursor.so"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
