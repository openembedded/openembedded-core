require xtrans_${PV}.bb

DEPENDS = "util-macros-native"
PE = "1"

XORG_PN = "xtrans"

SRC_URI = "${XORG_MIRROR}/${XORG_RELEASE}/lib/${XORG_PN}-${PV}.tar.bz2"

inherit native
