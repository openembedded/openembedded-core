require xorg-lib-common.inc
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"

DEPENDS = "util-macros-native"
PE = "1"

XORG_PN = "xtrans"
S = "${WORKDIR}/xtrans-${PV}"

inherit native autotools pkgconfig
