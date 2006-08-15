
LICENSE = "MIT"
PV = "0.0+cvs${SRCDATE}"

SECTION = "libs"
DEPENDS = "virtual/libx11 libxext"
DESCRIPTION = "X print extension library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xp"
S = "${WORKDIR}/Xp"

inherit autotools pkgconfig 

CFLAGS_append += " -I ${S}/include/X11/XprintUtil -I ${S}/include/X11/extensions"

do_stage() {
	autotools_stage_all
}
