require libx11_${PV}.bb

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11"

DEPENDS = "xproto-sdk xextproto-sdk libxau-sdk xtrans-sdk libxdmcp-sdk xcmiscproto-sdk xf86bigfontproto-sdk kbproto-sdk inputproto-sdk bigreqsproto-sdk util-macros-sdk"
PROVIDES = ""

inherit sdk
