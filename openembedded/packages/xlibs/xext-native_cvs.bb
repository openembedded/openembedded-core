SECTION = "unknown"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/xext"
include xext_${PV}.bb
inherit native
DEPENDS = "x11-native xextensions-native"

