require libx11_${PV}.bb

DEPENDS = "xproto-native xextproto-native libxau-native xtrans-native libxdmcp-native xcmiscproto-native xf86bigfontproto-native kbproto-native inputproto-native bigreqsproto-native util-macros-native"
PROVIDES = "virtual/libx11-native"

inherit native
