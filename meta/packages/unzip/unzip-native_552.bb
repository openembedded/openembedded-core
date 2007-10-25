SECTION = "console/utils"
inherit native
require unzip_${PV}.bb

do_stage() {
	install -d ${STAGING_BINDIR}
	install unzip ${STAGING_BINDIR}
}
