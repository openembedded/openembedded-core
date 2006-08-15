SECTION = "x11/base"
require libx11_cvs.bb
inherit native
DEPENDS = "xproto-native xextensions-native libxau-native xtrans-native libxdmcp-native"
PROVIDES = ""
