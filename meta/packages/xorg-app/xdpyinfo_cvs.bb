PV = "0.0+cvs${SRCDATE}"
LICENSE = "MIT"
DEPENDS = "virtual/libx11 libxext libxtst"
DESCRIPTION = "X display information utility"
SECTION = "x11/base"
PR = "r1"

SRC_URI = "${FREEDESKTOP_CVS}/xapps;module=xdpyinfo"
S = "${WORKDIR}/xdpyinfo"

inherit autotools pkgconfig 
