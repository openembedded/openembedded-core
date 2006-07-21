SECTION = "x11/libs"
LICENSE= "BSD-X"
DEPENDS = "libx11 damageext libxfixes xproto"
PROVIDES = "xdamage"
DESCRIPTION = "X Damage extension library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXdamage-${PV}.tar.bz2 \
	   file://m4.patch;patch=1 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXdamage-${PV}"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
