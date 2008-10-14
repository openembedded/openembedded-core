require xorg-lib-common.inc

DESCRIPTION = "X11 Input extension library"
DEPENDS += "libxext inputproto"
PR = "r0"
PE = "1"
PV = "1.1.99.1+git${SRCREV}"

SRC_URI = "git://anongit.freedesktop.org/git/xorg/lib/libXi;protocol=git"
S = "${WORKDIR}/git"

XORG_PN = "libXi"

