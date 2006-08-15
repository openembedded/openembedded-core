PV = "0.0+cvs${SRCDATE}"
LICENSE= "MIT"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11"
PROVIDES = "ice"
DESCRIPTION = "X11 ICE library"
PR = "r1"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=ICE"
S = "${WORKDIR}/ICE"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
