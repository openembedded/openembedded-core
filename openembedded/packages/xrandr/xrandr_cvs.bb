PV = "0.0cvs${SRCDATE}"
LICENSE= "BSD-X"
DEPENDS = "libxrandr libx11 libxext"
DESCRIPTION = "X Resize and Rotate extension command."
SECTION = "x11/base"

SRC_URI = "cvs://anoncvs:anoncvs@pdx.freedesktop.org/cvs/xapps;module=xrandr"
S = "${WORKDIR}/xrandr"

inherit autotools pkgconfig 
