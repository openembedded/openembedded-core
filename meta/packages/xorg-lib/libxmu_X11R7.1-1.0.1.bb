require xorg-lib-common.inc

DESCRIPTION = "X Window System miscellaneous utility library"
PRIORITY = "optional"

DEPENDS += " libxt libxext xextproto virtual/libx11"
PROVIDES = "xmu"

XORG_PN = "libXmu"

PACKAGES =+ "libxmuu libxmuu-dev"

FILES_libxmuu = "${libdir}/libXmuu.so.*"
FILES_libxmuu-dev = "${libdir}/libXmuu.so"

LEAD_SONAME = "libXmu"
