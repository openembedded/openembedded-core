DESCRIPTION = "auto-setup environment for lsb test"
SECTION = "console/utils"
PRIORITY = "required"
LICENSE = "GPLv2"
PR = "r0"

LIC_FILES_CHKSUM = "file://LSB_Setup.sh;md5=9cc166e6ee4b327fb94d6da63af9556c"

SRC_URI = "file://LSB_Setup.sh"

S=${WORKDIR}

do_install() {
        # Only install file if it has a contents
	install -d ${D}/usr/bin
        install -m 0644 ${S}/LSB_Setup.sh ${D}/usr/bin
}
