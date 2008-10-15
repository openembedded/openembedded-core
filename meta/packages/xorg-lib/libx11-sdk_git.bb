require libx11_${PV}.bb

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libx11-git"

DEPENDS = "xproto-sdk xextproto-sdk libxau-sdk xtrans-sdk libxdmcp-sdk xcmiscproto-sdk xf86bigfontproto-sdk kbproto-sdk inputproto-sdk bigreqsproto-sdk util-macros-sdk xproto-native"
PROVIDES = ""
PV = "1.1.99.1+gitr${SRCREV}"

inherit sdk

EXTRA_OECONF += "--without-xcb --disable-udc --disable-xcms --disable-xlocale"
