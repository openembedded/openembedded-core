require libx11_${PV}.bb

DEPENDS = "xproto-nativesdk xextproto-nativesdk libxau-nativesdk xtrans-nativesdk libxdmcp-nativesdk xcmiscproto-nativesdk xf86bigfontproto-nativesdk kbproto-nativesdk inputproto-nativesdk bigreqsproto-nativesdk util-macros-nativesdk xproto-native"
PROVIDES = "virtual/libx11-nativesdk"

inherit nativesdk
