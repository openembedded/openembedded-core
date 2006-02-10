SECTION = "x11/libs"
LICENSE= "BSD-X"
DEPENDS = "x11 fixesext"
DESCRIPTION = "X Fixes extension library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXfixes-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXfixes-${PV}"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
