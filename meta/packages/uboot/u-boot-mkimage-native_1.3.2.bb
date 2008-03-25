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

do_stage () {
        install -m 0755 tools/mkimage ${STAGING_BINDIR_NATIVE}/uboot-mkimage
        ln -sf ${STAGING_BINDIR_NATIVE}/uboot-mkimage ${STAGING_BINDIR_NATIVE}/mkimage
}
