PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT"
DEPENDS = "x11 xext xtst"
DESCRIPTION = "X display information utility"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
SECTION = "x11/base"
PR = "r1"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xapps;module=xdpyinfo"
S = "${WORKDIR}/xdpyinfo"

inherit autotools pkgconfig 
