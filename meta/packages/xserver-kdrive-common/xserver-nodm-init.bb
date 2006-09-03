DESCRIPTION = "Simple Xserver Init Script (no dm)"
LICENSE = "GPL"
SECTION = "x11"
PRIORITY = "optional"
PR = "r8"

SRC_URI = "file://xserver-nodm"
S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install xserver-nodm ${D}/etc/init.d
}    

inherit update-rc.d

INITSCRIPT_NAME = "xserver-nodm"
INITSCRIPT_PARAMS = "start 99 5 2 . stop 20 0 1 6 ."
