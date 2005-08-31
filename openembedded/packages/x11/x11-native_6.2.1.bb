SECTION = "x11/base"
include x11_${PV}.bb
inherit native
DEPENDS = "xproto-native xextensions-native xau-native xtrans-native libxdmcp-native"
PROVIDES = ""
