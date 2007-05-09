DESCRIPTION = "X protocol headers"
SECTION = "x11/libs"
LICENSE= "MIT-X"
PE = "1"

SRC_URI = "${XORG_MIRROR}/individual/proto/xproto-${PV}.tar.bz2"
S = "${WORKDIR}/xproto-${PV}"

inherit native autotools pkgconfig
