PV = "0.0+cvs${SRCDATE}"
LICENSE = "BSD-X"
SECTION = "x11/libs"
DEPENDS = "randrext virtual/libx11 libxrender libxext"
DESCRIPTION = "X Resize and Rotate extension library."
PR = "r1"

SRC_URI = "${FREEDESKTOP_CVS}/xlibs;module=Xrandr"
S = "${WORKDIR}/Xrandr"

inherit autotools pkgconfig 

do_stage() {
	autotools_stage_all
}
