SECTION = "unknown"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/xext"
include libxext_${PV}.bb
inherit native
DEPENDS = "libx11-native xextensions-native"
PROVIDES = ""
