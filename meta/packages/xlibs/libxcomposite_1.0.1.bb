SECTION = "x11/libs"
LICENSE= "BSD-X"
DEPENDS = "libx11 compositeext xextensions libxfixes"
PROVIDES = "xcomposite"
DESCRIPTION = "X Composite extension library."
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libXcomposite-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXcomposite-${PV}"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
