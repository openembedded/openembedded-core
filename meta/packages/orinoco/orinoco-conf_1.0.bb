DESCRIPTION = "PCMCIA-cs configuration files for Hermes (Orinoco) wireless LAN cards"
SECTION = "kernel/modules"
PRIORITY = "optional"
LICENSE = "GPL"
RDEPENDS = "update-modules"
PACKAGE_ARCH = "all"
PR = "r4"

SRC_URI = "file://orinoco_cs.conf"

do_install() {
        install -d ${D}${sysconfdir}/modutils
        install -m 0644 ${WORKDIR}/orinoco_cs.conf ${D}${sysconfdir}/modutils/
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
