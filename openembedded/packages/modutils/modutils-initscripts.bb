SECTION = "base"
DESCRIPTION = "modutils configuration files"
LICENSE = "PD"
SRC_URI = "file://modutils.sh"
PR = "r2"

INITSCRIPT_NAME = "modutils.sh"
INITSCRIPT_PARAMS = "start 20 S ."

inherit update-rc.d

do_compile () {
}

do_install () {
	install -d ${D}${sysconfdir}/init.d/
	install -m 0755 ${WORKDIR}/modutils.sh ${D}${sysconfdir}/init.d/
}
