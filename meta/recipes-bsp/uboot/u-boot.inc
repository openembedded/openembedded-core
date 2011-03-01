DESCRIPTION = "U-boot bootloader"
HOMEPAGE = "http://u-boot.sf.net"
SECTION = "bootloaders"
PRIORITY = "optional"
PROVIDES = "virtual/bootloader"

inherit deploy

PARALLEL_MAKE=""

# GCC 4.5.1 builds unusable binaries using -Os, remove it from OPTFLAGS
EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX} OPTFLAGS='-O2'"

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
