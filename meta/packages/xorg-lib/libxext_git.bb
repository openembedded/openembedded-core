require xorg-lib-common.inc

DESCRIPTION = "X11 miscellaneous extension library"
DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"
PE = "1"
PV = "1.0.99.1+gitr${SRCREV}"

XORG_PN = "libXext"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/${XORG_PN};protocol=git"
S = "${WORKDIR}/git/"
