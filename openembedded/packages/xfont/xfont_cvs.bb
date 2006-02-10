PV = "0.0+cvs${SRCDATE}"
LICENSE = "BSD-X"
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto xtrans zlib"
DESCRIPTION = "X font library (used by the X server)."

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xlibs;module=Xfont \
	file://scalable.patch;patch=1"
S = "${WORKDIR}/Xfont"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
