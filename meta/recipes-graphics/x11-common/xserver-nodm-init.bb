DESCRIPTION = "Simple Xserver Init Script (no dm)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "x11"
PR = "r31"
RDEPENDS_${PN} = "sudo xuser-account"

SRC_URI = "file://xserver-nodm \
           file://Xusername \
           file://gplv2-license.patch"

S = "${WORKDIR}"

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}${sysconfdir}
    install -d ${D}${sysconfdir}/init.d
    install xserver-nodm ${D}${sysconfdir}/init.d
    if [ "${ROOTLESS_X}" = "1" ] ; then
        install -d ${D}${sysconfdir}/X11
        install Xusername ${D}${sysconfdir}/X11
    fi
}

inherit update-rc.d

INITSCRIPT_NAME = "xserver-nodm"
INITSCRIPT_PARAMS = "start 9 5 2 . stop 20 0 1 6 ."

