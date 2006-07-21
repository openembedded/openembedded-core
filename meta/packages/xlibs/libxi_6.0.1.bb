DESCRIPTION = "X Input Extension library"
LICENSE = "MIT-X"
SECTION = "x11/libs"
DEPENDS = "xproto libx11 xextensions"
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXi-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXi-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}

