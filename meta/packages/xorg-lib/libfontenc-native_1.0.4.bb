require xorg-lib-common.inc
DESCRIPTION = "X libfontenc library (used by libxfont)."
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"

DEPENDS = "zlib-native xproto-native"

PE = "1"

XORG_PN = "libfontenc"
S = "${WORKDIR}/libfontenc-${PV}"

inherit native autotools pkgconfig
