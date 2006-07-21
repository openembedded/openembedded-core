SECTION = "x11/libs"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libxdmcp"
include libxdmcp_${PV}.bb
inherit native
DEPENDS = "xproto-native"
PROVIDES = ""
