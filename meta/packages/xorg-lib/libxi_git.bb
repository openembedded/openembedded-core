require xorg-lib-common.inc

DESCRIPTION = "X11 Input extension library"
DEPENDS += "libxext inputproto"
PE = "1"
PV = "1.9.99.5+gitr${SRCPV}"

XORG_PN = "libXi"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/${XORG_PN};protocol=git"
S = "${WORKDIR}/git"
