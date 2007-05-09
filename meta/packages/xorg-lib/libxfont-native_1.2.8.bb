DESCRIPTION = "X font library (used by the X server)."
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "BSD-X"
PE = "1"

DEPENDS = "xproto-native zlib-native fontcacheproto-native fontsproto-native libfontenc-native xtrans-native freetype-native util-macros-native"

SRC_URI = "${XORG_MIRROR}/individual/lib/libXfont-1.2.0.tar.bz2"
S = "${WORKDIR}/libXfont-1.2.0"

inherit native autotools pkgconfig

