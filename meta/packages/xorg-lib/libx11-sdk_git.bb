require libx11.inc
require libx11_git.inc

DEPENDS = "xproto-sdk xextproto-sdk libxau-sdk xtrans-sdk libxdmcp-sdk xcmiscproto-sdk xf86bigfontproto-sdk kbproto-sdk inputproto-sdk bigreqsproto-sdk util-macros-sdk xproto-native"
PROVIDES = ""

EXTRA_OECONF += "--without-xcb"

inherit sdk
