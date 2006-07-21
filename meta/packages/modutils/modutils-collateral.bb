SECTION = "base"
DESCRIPTION = "modutils configuration files"
PR = "r2"
LICENSE = "MIT"

SRC_URI = "file://modules \
	   file://modules.conf"

do_compile () {
}

do_install () {
	install -d ${D}${sysconfdir}
	install -m 0644 ${WORKDIR}/modules ${D}${sysconfdir}/modules
	install -m 0644 ${WORKDIR}/modules.conf ${D}${sysconfdir}/modules.conf
}
