SECTION = "libs"
LICENSE = "MIT-X"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DEPENDS = "libx11 libice"
DESCRIPTION = "Session management library"
PR = "r1"

SRC_URI = "${XLIBS_MIRROR}/libSM-${PV}.tar.bz2 \
	   file://autofoo.patch;patch=1"
S = "${WORKDIR}/libSM-${PV}"

inherit autotools pkgconfig 

do_stage () {
	autotools_stage_all
}
