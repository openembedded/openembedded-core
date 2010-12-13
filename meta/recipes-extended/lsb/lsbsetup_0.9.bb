DESCRIPTION = "auto-setup environment for lsb test"
SECTION = "console/utils"
PRIORITY = "required"
LICENSE = "GPLv2"
PR = "r0"


LIC_FILES_CHKSUM = "file://LSB_Setup.sh;md5=c7360d77e08a7f4f2fa66acf28012e7e"

SRC_URI = "file://LSB_Setup.sh"

S=${WORKDIR}

do_install() {
        # Only install file if it has a contents
        install -m 0644 ${S}/LSB_Setup.sh ${D}/usr/bin
}
