include m4_${PV}.bb

inherit native

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/m4"

INHIBIT_AUTOTOOLS_DEPS = "1"
DEPENDS += "gnu-config-native"

do_configure()  {
	install -m 0644 ${STAGING_DATADIR}/gnu-config/config.sub .
	install -m 0644 ${STAGING_DATADIR}/gnu-config/config.guess .
	oe_runconf
}

do_stage() {
	install -m 0755 src/m4 ${STAGING_BINDIR}/
}

