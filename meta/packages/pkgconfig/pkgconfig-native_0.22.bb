SECTION = "console/utils"
require pkgconfig_${PV}.bb
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/pkgconfig-${PV}"

S = "${WORKDIR}/pkg-config-${PV}/"
inherit native
DEPENDS = ""
