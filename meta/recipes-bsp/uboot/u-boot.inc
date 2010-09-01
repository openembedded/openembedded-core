DESCRIPTION = "U-boot bootloader"
HOMEPAGE = "http://u-boot.sf.net"
SECTION = "bootloaders"
PRIORITY = "optional"
LICENSE = "GPL"
PROVIDES = "virtual/bootloader"

inherit deploy

PARALLEL_MAKE=""

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"

UBOOT_MACHINE ?= "${MACHINE}_config"
UBOOT_IMAGE ?= "u-boot-${MACHINE}-${PV}-${PR}.bin"
UBOOT_SYMLINK ?= "u-boot-${MACHINE}.bin"

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake ${UBOOT_MACHINE}
	oe_runmake all
}

do_deploy () {
	install ${S}/u-boot.bin ${DEPLOYDIR}/${UBOOT_IMAGE}

	cd ${DEPLOYDIR}
	rm -f ${UBOOT_SYMLINK}
	ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}
}
addtask deploy before do_build after do_compile

do_install() {
	install -d ${D}${bindir}
	install -m 755 tools/mkimage ${D}${bindir}/
}
