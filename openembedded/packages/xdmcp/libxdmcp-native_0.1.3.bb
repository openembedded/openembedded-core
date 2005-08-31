SECTION = "x11/libs"
include libxdmcp_${PV}.bb
inherit native
DEPENDS = "xproto-native"
PROVIDES = ""
