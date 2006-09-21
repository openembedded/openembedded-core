SECTION = "x11/libs"
LICENSE= "MIT"
PRIORITY = "optional"
DEPENDS = "xproto"
PROVIDES = "xau"
DESCRIPTION = "Authorization Protocol for X."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXau-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXau-${PV}"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
