SECTION = "devel"
require autoconf_${PV}.bb
DEPENDS = "m4-native gnu-config-native"
RDEPENDS_${PN} = "m4-native gnu-config-native"

S = "${WORKDIR}/autoconf-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/autoconf-${PV}"

inherit native
