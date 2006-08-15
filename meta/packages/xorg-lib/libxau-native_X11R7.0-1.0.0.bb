SECTION = "x11/libs"
PRIORITY = "optional"
#MAINTAINER = ""
LICENSE = "BSD-X"

DEPENDS = "xproto-native util-macros-native"

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/lib/libXau-${PV}.tar.bz2"
S = "${WORKDIR}/libXau-${PV}"

inherit native autotools pkgconfig
