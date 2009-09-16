require libx11.inc
require libx11_git.inc

DEPENDS = "xproto-nativesdk xextproto-nativesdk libxau-nativesdk xtrans-nativesdk libxdmcp-nativesdk xcmiscproto-nativesdk xf86bigfontproto-nativesdk kbproto-nativesdk inputproto-nativesdk bigreqsproto-nativesdk util-macros-nativesdk xproto-native"
PROVIDES = "virtual/libx11-nativesdk"

EXTRA_OECONF += "--without-xcb"

inherit nativesdk
