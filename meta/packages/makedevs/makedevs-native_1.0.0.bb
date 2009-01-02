require makedevs_${PV}.bb
inherit native

do_stage() {
	install -d ${STAGING_BINDIR}/
        install -m 0755 ${S}/makedevs ${STAGING_BINDIR}/
}
