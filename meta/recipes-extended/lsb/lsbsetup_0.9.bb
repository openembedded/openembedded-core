DESCRIPTION = "auto-setup environment for lsb test"
SECTION = "console/utils"
PRIORITY = "required"
LICENSE = "GPLv2"
PR = "r0"

LIC_FILES_CHKSUM = "file://LSB_Setup.sh;md5=7391be3e70a02d44e1b183fa103b0585"

SRC_URI = "file://LSB_Setup.sh"

S=${WORKDIR}

do_install() {
        # Only install file if it has a contents
	install -d ${D}/usr/bin
        install -m 0644 ${S}/LSB_Setup.sh ${D}/usr/bin
}
