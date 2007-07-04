require libxau_${PV}.bb

DEPENDS = "xproto-native util-macros-native"
PR = "r1"

XORG_PN = "libXau"

inherit native
