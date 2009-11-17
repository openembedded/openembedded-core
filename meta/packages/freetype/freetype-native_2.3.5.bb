require freetype_${PV}.bb
inherit native

EXTRA_OEMAKE=

do_configure() {
	(cd builds/unix && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}
