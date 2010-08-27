DESCRIPTION = "opkg configuration files"
SECTION = "base"
LICENSE = "MIT"

SRC_URI = "file://opkg.conf.comments \
	   file://lists \
	   file://dest \
	   file://src "

do_compile () {
	cat ${WORKDIR}/opkg.conf.comments >${WORKDIR}/opkg.conf
	cat ${WORKDIR}/src	>>${WORKDIR}/opkg.conf
	cat ${WORKDIR}/dest	>>${WORKDIR}/opkg.conf
	cat ${WORKDIR}/lists	>>${WORKDIR}/opkg.conf
}

do_install () {
	install -d ${D}${sysconfdir}/
	install -m 0644 ${WORKDIR}/opkg.conf ${D}${sysconfdir}/opkg.conf
}

CONFFILES_${PN} = "${sysconfdir}/opkg.conf"
