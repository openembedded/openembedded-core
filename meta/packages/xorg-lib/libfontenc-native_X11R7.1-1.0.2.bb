DESCRIPTION = "X libfontenc library (used by libxfont)."
SECTION = "x11/libs"
PRIORITY = "optional"
#MAINTAINER = ""
LICENSE = "BSD-X"

DEPENDS = "zlib-native xproto-native"

SRC_URI = "${XORG_MIRROR}/X11R7.1/src/lib/libfontenc-${PV}.tar.bz2"
S = "${WORKDIR}/libfontenc-${PV}"

inherit native autotools pkgconfig
