DESCRIPTION = "Quick hack to start gadget Ethernet on the 770"
LICENSE = "GPL"
SECTION = "x11"
PRIORITY = "optional"
PR = "r0"

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
