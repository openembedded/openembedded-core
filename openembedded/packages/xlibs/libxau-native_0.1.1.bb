SECTION = "x11/base"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/xau"
include libxau_${PV}.bb
inherit native
DEPENDS = "xproto-native"
PROVIDES = ""
