SECTION = "base"
include makedevs_${PV}.bb
inherit native 

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/makedevs-${PV}"

do_stage() {
	install -d ${STAGING_BINDIR}/
        install -m 0755 ${S}/makedevs ${STAGING_BINDIR}/
}
