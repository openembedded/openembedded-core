SECTION = "x11/libs"
LICENSE = "BSD-X"
PRIORITY = "optional"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "xproto xtrans zlib"
PROVIDES = "xfont"
DESCRIPTION = "X font library (used by the X server)."
PR = "r2"

SRC_URI = "${XLIBS_MIRROR}/libXfont-${PV}.tar.bz2 \
	file://scalable.patch;patch=1 \
	file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXfont-${PV}"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
