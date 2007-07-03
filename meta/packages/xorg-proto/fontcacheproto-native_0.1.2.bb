require xorg-proto-common.inc
DESCRIPTION = "X protocol headers"
SECTION = "x11/libs"
LICENSE = "MIT-X"
PE = "1"

XORG_PN = "fontcacheproto"
S = "${WORKDIR}/fontcacheproto-${PV}"

inherit native autotools pkgconfig
