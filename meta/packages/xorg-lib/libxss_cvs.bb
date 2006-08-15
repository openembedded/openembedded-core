LICENSE = "GPL"

PV = "0.0+cvs${SRCDATE}"
PR = "r2"

SECTION = "libs"
DEPENDS = "virtual/libx11 libxext"
DESCRIPTION = "X screen saver extension library."

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xss"
S = "${WORKDIR}/Xss"

inherit autotools pkgconfig 

#CFLAGS_append += " -I ${S}/include/X11/XprintUtil -I ${S}/include/X11/extensions"

do_stage() {
	autotools_stage_all
}
