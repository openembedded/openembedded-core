PV = "0.0+cvs${SRCDATE}"
LICENSE= "BSD-X"
DEPENDS = "libxrandr x11 xext"
DESCRIPTION = "X Resize and Rotate extension command."
SECTION = "x11/base"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xapps;module=xrandr"
S = "${WORKDIR}/xrandr"

inherit autotools pkgconfig 
