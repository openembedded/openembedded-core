SECTION = "devel"
include automake_${PV}.bb
DEPENDS = "autoconf-native"

inherit native

do_stage () {
	oe_runmake install
	install -d ${datadir}/aclocal
}
