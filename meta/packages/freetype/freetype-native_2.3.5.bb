require freetype_${PV}.bb
inherit native

EXTRA_OEMAKE=

do_configure() {
	(cd builds/unix && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_stage() {
	autotools_stage_includes
	oe_libinstall -so -a -C objs libfreetype ${STAGING_LIBDIR}
}

