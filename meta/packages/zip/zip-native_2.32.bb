require zip_${PV}.bb

inherit native

do_stage() {
	install -d ${STAGING_BINDIR}
	install zip zipnote zipsplit zipcloak ${STAGING_BINDIR}
}
