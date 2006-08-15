SECTION = "x11/libs"
MAINTAINER = "Greg Gilbert <greg@treke.net>"
DEPENDS = "virtual/libx11 xextensions libxext resourceext"
PROVIDES = "xres"
DESCRIPTION = "X Resource usage library."
LICENSE = "X-MIT"
SRC_URI = "${XLIBS_MIRROR}/libXres-${PV}.tar.bz2"
S = "${WORKDIR}/libXres-${PV}"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
