require xorg-lib-common.inc
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"

DEPENDS = "xproto-native util-macros-native"

XORG_PN = "libXau"
S = "${WORKDIR}/libXau-${PV}"

inherit native autotools pkgconfig
