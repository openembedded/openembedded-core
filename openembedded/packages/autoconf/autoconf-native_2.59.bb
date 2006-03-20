SECTION = "devel"
include autoconf_${PV}.bb
DEPENDS = "m4-native gnu-config-native"
RDEPENDS_${PN} = "m4-native gnu-config-native"
RRECOMMENDS_${PN} = "automake-native"

S = "${WORKDIR}/autoconf-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/autoconf-${PV}"

inherit native
