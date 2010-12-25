DESCRIPTION = "U-boot bootloader mkimage tool"
LICENSE = "GPL"
SECTION = "bootloader"

SRC_URI = "ftp://ftp.denx.de/pub/u-boot/u-boot-${PV}.tar.bz2 \
           file://fix-arm920t-eabi.patch;patch=1"

LIC_FILES_CHKSUM = "file://COPYING;md5=4c6cde5df68eff615d36789dc18edd3b"

S = "${WORKDIR}/u-boot-${PV}"

inherit native

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"

do_compile () {
	oe_runmake smdk2410_config
	oe_runmake tools
}

do_install () {
	install -d ${D}${bindir}/
	install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
	ln -sf uboot-mkimage ${D}${bindir}/mkimage
}
