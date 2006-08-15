SECTION = "unknown"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/xext"
require libxext_${PV}.bb
inherit native
DEPENDS = "virtual/libx11-native xextensions-native"
PROVIDES = ""
