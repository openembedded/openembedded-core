DESCRIPTION = "Xinerama library"
LICENSE = "MIT"
SECTION = "x11/libs"
PRIORITY = "optional"
MAINTAINER = "Phil Blundell <pb@handhelds.org>"
DEPENDS = "panoramixext xproto virtual/libx11 libxext"
PROVIDES = "xinerama"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xinerama;date=20050505"
S = "${WORKDIR}/Xinerama"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
