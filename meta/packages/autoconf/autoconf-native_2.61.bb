require autoconf_${PV}.bb

DEPENDS = "m4-native gnu-config-native"
RDEPENDS_${PN} = "m4-native gnu-config-native"

SRC_URI += "file://fix_path_xtra.patch;patch=1"

S = "${WORKDIR}/autoconf-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/autoconf-${PV}"

inherit native
