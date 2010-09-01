DESCRIPTION = "PCMCIA-cs configuration files for wireless LAN cards based on Intersil's Prism2/2.5/3 chipset"
SECTION = "kernel/modules"
PRIORITY = "optional"
LICENSE = "GPL"
RDEPENDS = "update-modules"
PACKAGE_ARCH = "all"
PR = "r10"

SRC_URI = "file://hostap_cs.modalias"

do_compile() {
}

do_install() {
        install -d ${D}${sysconfdir}/modutils

	install -m 0644 ${WORKDIR}/hostap_cs.modalias ${D}${sysconfdir}/modutils/hostap_cs.conf
}

pkg_postinst () {
	if [ -n "$D" ]; then
		exit 1
	fi
	update-modules || true
}

pkg_postrm () {
	update-modules || true
}
