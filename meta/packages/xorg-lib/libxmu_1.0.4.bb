require xorg-lib-common.inc

DESCRIPTION = "X11 miscellaneous utility library"
DEPENDS += "libxt libxext"
PROVIDES = "xmu"
PR = "r1"
PE = "1"

XORG_PN = "libXmu"

LEAD_SONAME = "libXmu"

PACKAGES =+ "libxmuu libxmuu-dev"

FILES_libxmuu = "${libdir}/libXmuu.so.*"
FILES_libxmuu-dev = "${libdir}/libXmuu.so"
