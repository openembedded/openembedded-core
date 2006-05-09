DESCRIPTION = "PCMCIA-cs configuration files for wireless LAN cards based on Intersil's Prism2/2.5/3 chipset"
MAINTAINER = "Marcin Juszkiewicz <openembedded@hrw.one.pl>"
SECTION = "kernel/modules"
PRIORITY = "optional"
LICENSE = "GPL"
PACKAGE_ARCH = "all"
PR = "r4"

SRC_URI = "file://hostap_cs.conf \
           file://hostap_cs.conf-upstream"

do_install() {   
        install -d ${D}${sysconfdir}/pcmcia
        install -m 0644 ${WORKDIR}/hostap_cs.conf-upstream ${D}${sysconfdir}/pcmcia/hostap_cs.conf
	cat ${WORKDIR}/hostap_cs.conf >>${D}${sysconfdir}/pcmcia/hostap_cs.conf
}
