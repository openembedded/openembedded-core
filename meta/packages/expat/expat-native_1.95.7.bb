SECTION = "libs"
include expat_${PV}.bb
inherit native
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/expat-${PV}"
