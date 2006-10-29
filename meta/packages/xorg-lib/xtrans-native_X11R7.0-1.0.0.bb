SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"

DEPENDS = "util-macros-native"

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/lib/xtrans-${PV}.tar.bz2"
S = "${WORKDIR}/xtrans-${PV}"

inherit native autotools pkgconfig
