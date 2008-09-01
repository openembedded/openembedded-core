require libxml2.inc

PR = "r6"

DEPENDS += "python-native-runtime"

EXTRA_OECONF = "--with-python=${STAGING_DIR_NATIVE}/${prefix} --without-debug --without-legacy --with-catalog --without-docbook --with-c14n"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/libxml2-${PV}"
S = "${WORKDIR}/libxml2-${PV}"

inherit native

do_stage () {
	oe_runmake install
}
