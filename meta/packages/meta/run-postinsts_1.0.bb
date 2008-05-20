DESCRIPTION = "Run postinstall scripts on device using awk"
SECTION = "devel"
PR = "r6"

SRC_URI = "file://run-postinsts file://run-postinsts.awk"

INITSCRIPT_NAME = "run-postinsts"
INITSCRIPT_PARAMS = "start 98 S ."

inherit update-rc.d

do_configure() {
	:
}

do_compile () {
	:
}

do_install() {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/run-postinsts ${D}${sysconfdir}/init.d/

	install -d ${D}${datadir}/${PN}/
	install -m 0644 ${WORKDIR}/run-postinsts.awk ${D}${datadir}/${PN}/
}

do_stage () {
	:
}
