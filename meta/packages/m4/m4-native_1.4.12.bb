require m4_${PV}.bb
inherit native

PR = "r1"

SRC_URI_append = " file://ac_config_links.patch;patch=1"

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

