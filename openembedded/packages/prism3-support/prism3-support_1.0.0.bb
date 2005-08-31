DESCRIPTION = "meta-package for prism3 support through ifupdown and hostap_fw_load"
SECTION = "base"
LICENSE = "GPL"
DEPENDS = "prism3-firmware hostap-utils"
RDEPENDS = "prism3-firmware hostap-utils"
MAINTAINER = "Michael 'Mickey' Lauer <mickey@Vanille.de>"
PR = "r0"

SRC_URI = "file://hostap-fw-load"

do_install() {
	install -d ${D}${sysconfdir}/network/if-pre-up.d/
	install -m 0755 ${WORKDIR}/hostap-fw-load ${D}${sysconfdir}/network/if-pre-up.d/
}

