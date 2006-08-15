LICENSE = "GPL"
PV = "0.0+cvs${SRCDATE}"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "virtual/libx11"
DESCRIPTION = "X11 keyboard library"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=xkbfile"
S = "${WORKDIR}/xkbfile"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
