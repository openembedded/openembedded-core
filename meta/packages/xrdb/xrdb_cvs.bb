PV = "0.0+cvs${SRCDATE}"
DEPENDS = "libx11 libxmu libxext"
DESCRIPTION = "X server resource database utility"
MAINTAINER = "Rene Wagner <rw@handhelds.org>"
SECTION = "x11/base"
LICENSE = "xrdb"
PR = "r2"

SRC_URI = "${FREEDESKTOP_CVS}/xorg;module=xc/programs/xrdb \
           file://autofoo.patch;patch=1"
S = "${WORKDIR}/xrdb"

inherit autotools pkgconfig 
