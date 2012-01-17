require m4-${PV}.inc

INHIBIT_AUTOTOOLS_DEPS = "1"
DEPENDS += "gnu-config-native"

do_configure()  {
	install -m 0644 ${STAGING_DATADIR}/gnu-config/config.sub .
	install -m 0644 ${STAGING_DATADIR}/gnu-config/config.guess .
	oe_runconf
}

inherit native
