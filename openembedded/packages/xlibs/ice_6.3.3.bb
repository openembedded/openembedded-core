SECTION = "libs"
LICENSE= "MIT"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DEPENDS = "x11"
DESCRIPTION = "X11 ICE library"
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libICE-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libICE-${PV}"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
