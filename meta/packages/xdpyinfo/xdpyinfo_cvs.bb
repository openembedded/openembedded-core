PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT"
DEPENDS = "libx11 libxext libxtst"
DESCRIPTION = "X display information utility"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
SECTION = "x11/base"
PR = "r1"

SRC_URI = "${FREEDESKTOP_CVS}/xapps;module=xdpyinfo"
S = "${WORKDIR}/xdpyinfo"

inherit autotools pkgconfig 
