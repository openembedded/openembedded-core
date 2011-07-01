DESCRIPTION = "PCMCIA-cs configuration files for Hermes (Orinoco) wireless LAN cards"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
RDEPENDS_${PN} = "update-modules"
PR = "r7"

SRC_URI = "file://orinoco_cs.conf \
           file://COPYING.patch"

inherit allarch

do_install() {
        install -d ${D}${sysconfdir}/modutils
        install -m 0644 ${WORKDIR}/orinoco_cs.conf ${D}${sysconfdir}/modutils/
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
