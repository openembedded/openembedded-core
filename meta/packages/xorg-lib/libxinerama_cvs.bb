DESCRIPTION = "Xinerama library"
LICENSE = "MIT"
SECTION = "x11/libs"
PRIORITY = "optional"
DEPENDS = "panoramixext xproto virtual/libx11 libxext"
PROVIDES = "xinerama"
PV = "0.0+cvs${SRCDATE}"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xinerama"
S = "${WORKDIR}/Xinerama"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
