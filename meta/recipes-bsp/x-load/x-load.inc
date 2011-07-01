DESCRIPTION = "x-load bootloader loader"
SECTION = "bootloaders"
LICENSE = "GPL"

inherit deploy

DEPENDS = ""

PARALLEL_MAKE=""

EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX}"

XLOAD_MACHINE ?= "${MACHINE}_config"

XLOAD_IMAGE ?= "x-load-${MACHINE}-${PV}-${PR}.bin.ift"
XLOAD_SYMLINK ?= "x-load-${MACHINE}.bin.ift"
MLO_IMAGE ?= "MLO-${MACHINE}-${PV}-${PR}"
MLO_SYMLINK ?= "MLO-${MACHINE}"

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake distclean
	oe_runmake ${XLOAD_MACHINE}
	oe_runmake ift
}

do_deploy () {
	install ${S}/x-load.bin.ift ${DEPLOYDIR}/${XLOAD_IMAGE}
	install ${S}/x-load.bin.ift ${DEPLOYDIR}/${MLO_IMAGE}

	cd ${DEPLOYDIR}
	rm -f ${XLOAD_SYMLINK}
	ln -sf ${XLOAD_IMAGE} ${XLOAD_SYMLINK}
	rm -f ${MLO_SYMLINK}
	ln -sf ${MLO_IMAGE} ${MLO_SYMLINK}
}
addtask deploy before do_build after do_compile

