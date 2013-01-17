DESCRIPTION = "PCMCIA-cs configuration files for Hermes (Orinoco) wireless LAN cards"
SECTION = "kernel/modules"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=393a5ca445f6965873eca0259a17f833"
PR = "r8"

SRC_URI = "file://orinoco_cs.conf \
           file://COPYING.patch"

inherit allarch

do_install() {
        install -d ${D}${sysconfdir}/modprobe.d
        install -m 0644 ${WORKDIR}/orinoco_cs.conf ${D}${sysconfdir}/modprobe.d/
}

