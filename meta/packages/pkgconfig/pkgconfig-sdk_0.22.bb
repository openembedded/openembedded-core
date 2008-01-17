require pkgconfig.inc
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/pkgconfig-${PV}"

SRC_URI += "file://autofoo.patch;patch=1"

S = "${WORKDIR}/pkg-config-${PV}/"
inherit sdk
DEPENDS = ""
