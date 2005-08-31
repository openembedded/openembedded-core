SECTION = "unknown"
include popt_${PV}.bb
inherit native

S = "${WORKDIR}/popt-${PV}"
FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/popt-${PV}"
