SECTION = "x11/base"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11"
include libx11_${PV}.bb
inherit native
DEPENDS = "xproto-native xextensions-native libxau-native xtrans-native libxdmcp-native"
PROVIDES = ""
