DESCRIPTION = "X font library (used by the X server)."
SECTION = "x11/libs"
PRIORITY = "optional"
#MAINTAINER = ""
LICENSE = "BSD-X"

DEPENDS = "xproto-native zlib-native fontcacheproto-native fontsproto-native libfontenc-native xtrans-native freetype-native util-macros-native"

SRC_URI = "${XORG_MIRROR}/X11R7.0/src/lib/libXfont-${PV}.tar.bz2"
S = "${WORKDIR}/libXfont-${PV}"

inherit native autotools pkgconfig

