DESCRIPTION = "U-boot bootloader mkimage tool"
LICENSE = "GPL"
SECTION = "bootloader"

PR = "r0"

SRC_URI = "ftp://ftp.denx.de/pub/u-boot/u-boot-${PV}.tar.bz2"

S = "${WORKDIR}/u-boot-${PV}"

EXTRA_OEMAKE = "${@['CROSS_COMPILE=${TARGET_PREFIX}',''][bb.data.getVar('TARGET_PREFIX', d, 1) == '']}"

BBCLASSEXTEND = "native nativesdk"

do_compile () {
 	oe_runmake smdk2410_config
        oe_runmake tools
}

do_install () {
	install -d ${D}${bindir}
	install -m 0755 tools/mkimage ${D}${bindir}/
}

do_stage_uboot () {
	install -d ${STAGING_BINDIR}
        install -m 0755 tools/mkimage ${STAGING_BINDIR}/uboot-mkimage
        ln -sf uboot-mkimage ${STAGING_BINDIR}/mkimage
}

do_stage () {
	do_stage_uboot
}

# do_stage is override is overridden by native.bbclass
# so we have to specifically override it for virtclass-native also...
do_stage_virtclass-native () {
	do_stage_uboot
}
