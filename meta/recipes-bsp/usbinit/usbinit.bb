DESCRIPTION = "Initscript to manage gadget Ethernet"

LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://${WORKDIR}/COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PR = "r2"

SRC_URI = "file://usb-gether \
           file://COPYING.GPL"
S = "${WORKDIR}"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install usb-gether ${D}/etc/init.d
}

inherit update-rc.d allarch

INITSCRIPT_NAME = "usb-gether"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
