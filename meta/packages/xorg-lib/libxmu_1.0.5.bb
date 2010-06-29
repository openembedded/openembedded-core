DESCRIPTION = "X11 miscellaneous utility library"

require xorg-lib-common.inc

LICENSE = "MIT & MIT-style"
LIC_FILES_CHKSUM = "file://COPYING;md5=bb8d1df693806ad17c7268086f1d5394"

DEPENDS += "libxt libxext"
PROVIDES = "xmu"

PR = "r0"
PE = "1"

XORG_PN = "libXmu"

LEAD_SONAME = "libXmu"

PACKAGES =+ "libxmuu libxmuu-dev"

FILES_libxmuu = "${libdir}/libXmuu.so.*"
FILES_libxmuu-dev = "${libdir}/libXmuu.so"
