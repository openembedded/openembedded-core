require xorg-proto-common.inc
DESCRIPTION = "X protocol headers"
SECTION = "x11/libs"
LICENSE = "MIT-X"
PE = "1"

XORG_PN = "xf86bigfontproto"
S = "${WORKDIR}/xf86bigfontproto-${PV}"

inherit native autotools pkgconfig
