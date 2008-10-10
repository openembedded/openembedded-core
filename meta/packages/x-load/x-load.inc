DESCRIPTION = "x-load bootloader loader"
SECTION = "bootloaders"
PRIORITY = "optional"
LICENSE = "GPL"

DEPENDS = "signgp-native"

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
	oe_runmake
}

do_deploy () {
	signGP ${S}/x-load.bin
	install -d ${DEPLOY_DIR_IMAGE}
	install ${S}/x-load.bin.ift ${DEPLOY_DIR_IMAGE}/${XLOAD_IMAGE}
	install ${S}/x-load.bin.ift ${DEPLOY_DIR_IMAGE}/${MLO_IMAGE}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${XLOAD_IMAGE}
	package_stagefile_shell	${DEPLOY_DIR_IMAGE}/${MLO_IMAGE}

	cd ${DEPLOY_DIR_IMAGE}
	rm -f ${XLOAD_SYMLINK}
	ln -sf ${XLOAD_IMAGE} ${XLOAD_SYMLINK}
	rm -f ${MLO_SYMLINK}
	ln -sf ${MLO_IMAGE} ${MLO_SYMLINK}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${XLOAD_SYMLINK}
	package_stagefile_shell ${DEPLOY_DIR_IMAGE}/${MLO_SYMLINK}
}
do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_compile

