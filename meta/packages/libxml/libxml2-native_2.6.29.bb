require libxml2.inc

PR = "r4"

DEPENDS += "python-native-runtime"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libxml2-${PV}"
S = "${WORKDIR}/libxml2-${PV}"

inherit native

do_stage () {
	oe_runmake install
}
