require libx11.inc
require libx11_git.inc

DEPENDS = "xproto-sdk xextproto-sdk libxau-sdk xtrans-sdk libxdmcp-sdk xcmiscproto-sdk xf86bigfontproto-sdk kbproto-sdk inputproto-sdk bigreqsproto-sdk util-macros-sdk xproto-native"
PROVIDES = "virtual/libx11-sdk"

EXTRA_OECONF += "--without-xcb"

inherit sdk
