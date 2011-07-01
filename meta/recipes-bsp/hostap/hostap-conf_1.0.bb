DESCRIPTION = "PCMCIA-cs configuration files for wireless LAN cards based on Intersil's Prism2/2.5/3 chipset"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
RDEPENDS_${PN} = "update-modules"
PR = "r14"

SRC_URI = "file://hostap_cs.modalias \
           file://COPYING.patch"

inherit allarch

do_compile() {
}

do_install() {
        install -d ${D}${sysconfdir}/modutils

	install -m 0644 ${WORKDIR}/hostap_cs.modalias ${D}${sysconfdir}/modutils/hostap_cs.conf
}

pkg_postinst_${PN} () {
	if [ -n "$D" ]; then
		exit 1
	fi
	update-modules || true
}

pkg_postrm_${PN} () {
	update-modules || true
}
