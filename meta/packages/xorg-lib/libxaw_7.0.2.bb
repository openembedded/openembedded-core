SECTION = "x11/libs"
LICENSE = "MIT"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DEPENDS = "xproto virtual/libx11 libxt libxmu libxpm"
PROVIDES = "xaw"
DESCRIPTION = "X Athena Widgets library"

SRC_URI = "${XLIBS_MIRROR}/libXaw-${PV}.tar.bz2 \
	   file://auxdir.patch;patch=1"

S = "${WORKDIR}/libXaw-${PV}"

inherit autotools pkgconfig 

# FIXME: libXaw needs a full x11, not diet
BROKEN = "1"

do_stage () {
	autotools_stage_all
}
