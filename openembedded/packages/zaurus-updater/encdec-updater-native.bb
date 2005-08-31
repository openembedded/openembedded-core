include encdec-updater.bb
inherit native

do_stage() {
	install -m 0755 encdec-updater ${STAGING_BINDIR}
}
