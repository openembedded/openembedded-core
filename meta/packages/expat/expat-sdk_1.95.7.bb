SECTION = "libs"
require expat_${PV}.bb
inherit sdk
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/expat-${PV}"
