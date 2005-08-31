SECTION = "console/utils"
include pkgconfig_${PV}.bb
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/pkgconfig-${PV}"

S = "${WORKDIR}/pkgconfig-${PV}"
inherit native
DEPENDS = ""
