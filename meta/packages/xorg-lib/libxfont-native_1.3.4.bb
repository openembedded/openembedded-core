require libxfont_${PV}.bb

DEPENDS = "xproto-native zlib-native fontcacheproto-native fontsproto-native \
           libfontenc-native xtrans-native freetype-native util-macros-native"
PE = "1"

# no need for patch used in libxfont
SRC_URI = "${XORG_MIRROR}/individual/lib/${XORG_PN}-${PV}.tar.bz2"

inherit native
