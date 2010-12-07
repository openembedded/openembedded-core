DESCRIPTION = "Simple Xserver Init Script (no dm)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "x11"
PRIORITY = "optional"
PR = "r23"
RDEPENDS_${PN} = "dbus-wait sudo"

SRC_URI = "file://xserver-nodm \
           file://gplv2-license.patch"

S = ${WORKDIR}

PACKAGE_ARCH = "all"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install xserver-nodm ${D}/etc/init.d
}

inherit update-rc.d

INITSCRIPT_NAME = "xserver-nodm"
INITSCRIPT_PARAMS = "start 9 5 2 . stop 20 0 1 6 ."
