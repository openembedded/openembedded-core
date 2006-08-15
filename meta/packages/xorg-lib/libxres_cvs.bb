PV = "0.0+cvs${SRCDATE}"
SECTION = "x11/libs"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "virtual/libx11 xextensions libxext resourceext"
PROVIDES = "xres"
DESCRIPTION = "X Resource usage library."
LICENSE = "X-MIT"
SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=XRes"
S = "${WORKDIR}/XRes"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
