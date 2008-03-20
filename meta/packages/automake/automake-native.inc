require automake.inc

DEPENDS = "autoconf-native"
RDEPENDS_automake-native = "autoconf-native perl-native-runtime"

inherit native

do_stage () {
	oe_runmake install
	install -d ${datadir}/aclocal
}
