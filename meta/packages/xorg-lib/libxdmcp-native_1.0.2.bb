SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"
PE = "1"

DEPENDS = "xproto-native util-macros-native"

SRC_URI = "${XORG_MIRROR}/individual/lib/libXdmcp-${PV}.tar.bz2"
S = "${WORKDIR}/libXdmcp-${PV}"

inherit native autotools pkgconfig
