DESCRIPTION = "U-boot bootloader mkimage tool"
LICENSE = "GPL"
SECTION = "bootloader"

SRC_URI = "ftp://ftp.denx.de/pub/u-boot/u-boot-${PV}.tar.bz2 \
           file://fix-arm920t-eabi.patch;patch=1"

S = "${WORKDIR}/u-boot-${PV}"

inherit native

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"

do_compile () {
	oe_runmake smdk2410_config
	oe_runmake tools
}

NATIVE_INSTALL_WORKS = "1"
do_install () {
	install -d ${D}${bindir}/
	install -m 0755 tools/mkimage ${D}${bindir}/uboot-mkimage
	ln -sf uboot-mkimage ${D}${bindir}/mkimage
}
