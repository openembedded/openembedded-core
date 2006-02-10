SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DEPENDS = "x11 libsm"
DESCRIPTION = "X Toolkit Intrinsics"
LICENSE = "X-MIT"
PR = "r1"
SRC_URI = "${XLIBS_MIRROR}/libXt-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libXt-${PV}"

PARALLEL_MAKE = ""

inherit autotools pkgconfig 

#nasty hack as utils need to be native
do_compile() {
        (
                unset CC LD CXX CCLD
#               unset CFLAGS CPPFLAGS CXXFLAGS LDFLAGS
                oe_runmake -C util 'CFLAGS=' 'LDFLAGS=' 'CXXFLAGS=' 'CPPFLAGS=' makestrs
        )
        oe_runmake
}

do_stage () {
	autotools_stage_all
}
