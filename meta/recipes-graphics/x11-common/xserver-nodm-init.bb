DESCRIPTION = "Simple Xserver Init Script (no dm)"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=751419260aa954499f7abaabaa882bbe"
SECTION = "x11"
PR = "r28"
RDEPENDS_${PN} = "sudo"

SRC_URI = "file://xserver-nodm \
           file://Xusername \
           file://gplv2-license.patch"

S = ${WORKDIR}

PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
    install -d ${D}/etc
    install -d ${D}/etc/init.d
    install xserver-nodm ${D}/etc/init.d
    if [ "${ROOTLESS_X}" = "1" ] ; then
        install -d ${D}/etc/X11
        install Xusername ${D}/etc/X11
    fi
}

inherit update-rc.d useradd

INITSCRIPT_NAME = "xserver-nodm"
INITSCRIPT_PARAMS = "start 9 5 2 . stop 20 0 1 6 ."

# Use fixed Xusername of xuser for now, this will need to be
# fixed if the Xusername changes from xuser
USERADD_PACKAGES = "${PN}"
USERADD_PARAM_${PN} = "--system --no-create-home \
                       --shell /bin/false --groups video,tty,audio \
                       --user-group xuser"

