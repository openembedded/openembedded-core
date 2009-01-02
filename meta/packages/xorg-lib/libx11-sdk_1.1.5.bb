require libx11_${PV}.bb

DEPENDS = "xproto-sdk xextproto-sdk libxau-sdk xtrans-sdk libxdmcp-sdk xcmiscproto-sdk xf86bigfontproto-sdk kbproto-sdk inputproto-sdk bigreqsproto-sdk util-macros-sdk xproto-native"
PROVIDES = ""

inherit sdk
