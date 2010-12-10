DESCRIPTION = "Initscript to manage gadget Ethernet"

LICENSE = "GPL"
LIC_FILES_CHKSUM = "file://COPYING.GPL;md5=751419260aa954499f7abaabaa882bbe"

PRIORITY = "optional"
PR = "r1"

SRC_URI = "file://usb-gether"
S = "${WORKDIR}"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install usb-gether ${D}/etc/init.d
}    

inherit update-rc.d

INITSCRIPT_NAME = "usb-gether"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
