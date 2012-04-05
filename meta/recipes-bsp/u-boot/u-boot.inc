DESCRIPTION = "U-Boot - the Universal Boot Loader"
HOMEPAGE = "http://www.denx.de/wiki/U-Boot/WebHome"
SECTION = "bootloaders"
PROVIDES = "virtual/bootloader"

inherit deploy

PARALLEL_MAKE=""

# GCC 4.5.1 builds unusable binaries using -Os, remove it from OPTFLAGS
EXTRA_OEMAKE = "CROSS_COMPILE=${TARGET_PREFIX} OPTFLAGS='-O2'"

python () {
	if not d.getVar("UBOOT_MACHINE", True):
		PN = d.getVar("PN", True)
		FILE = os.path.basename(d.getVar("FILE", True))
		bb.debug(1, "To build %s, see %s for instructions on \
			     setting up your machine config" % (PN, FILE))
		raise bb.parse.SkipPackage("because UBOOT_MACHINE is not set")
}

# Some versions of u-boot use .bin and others use .img.  By default use .bin
# but enable individual recipes to change this value.
UBOOT_SUFFIX ?= "bin"
UBOOT_IMAGE ?= "u-boot-${MACHINE}-${PV}-${PR}.${UBOOT_SUFFIX}"
UBOOT_BINARY ?= "u-boot.${UBOOT_SUFFIX}"
UBOOT_SYMLINK ?= "u-boot-${MACHINE}.${UBOOT_SUFFIX}"
UBOOT_MAKE_TARGET ?= "all"

# Some versions of u-boot build an SPL (Second Program Loader) image that
# should be packaged along with the u-boot binary as well as placed in the
# deploy directory.  For those versions they can set the following variables
# to allow packaging the SPL.
SPL_BINARY ?= ""
SPL_IMAGE ?= "${SPL_BINARY}-${MACHINE}-${PV}-${PR}"
SPL_SYMLINK ?= "${SPL_BINARY}-${MACHINE}"

do_compile () {
	unset LDFLAGS
	unset CFLAGS
	unset CPPFLAGS
	oe_runmake ${UBOOT_MACHINE}
	oe_runmake ${UBOOT_MAKE_TARGET}
}

do_install () {
    install -d ${D}/boot
    install ${S}/${UBOOT_BINARY} ${D}/boot/${UBOOT_IMAGE}
    ln -sf ${UBOOT_IMAGE} ${D}/boot/${UBOOT_BINARY}

    if [ "x${SPL_BINARY}" != "x" ]
    then
        install ${S}/${SPL_BINARY} ${D}/boot/${SPL_IMAGE}
        ln -sf ${SPL_IMAGE} ${D}/boot/${SPL_BINARY}
    fi
}

FILES_${PN} = "/boot"

do_deploy () {
    install -d ${DEPLOYDIR}
	install ${S}/${UBOOT_BINARY} ${DEPLOYDIR}/${UBOOT_IMAGE}

	cd ${DEPLOYDIR}
	rm -f ${UBOOT_SYMLINK}
	ln -sf ${UBOOT_IMAGE} ${UBOOT_SYMLINK}

    if [ "x${SPL_BINARY}" != "x" ]
    then
        install ${S}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_IMAGE}
        rm -f ${DEPLOYDIR}/${SPL_BINARY} ${DEPLOYDIR}/${SPL_SYMLINK}
        ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_BINARY}
        ln -sf ${SPL_IMAGE} ${DEPLOYDIR}/${SPL_SYMLINK}
    fi
}

addtask deploy before do_build after do_compile
