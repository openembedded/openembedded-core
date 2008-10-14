require xorg-lib-common.inc

DESCRIPTION = "X11 miscellaneous extension library"
DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"
PR = "r0"
PE = "1"
PV = "1.0.99.1+git${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libXext;protocol=git"
S = "${WORKDIR}/git"

XORG_PN = "libXext"
