require u-boot.inc

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/u-boot-omap3-git/${MACHINE}"

SRCREV = "f40f6db278f602b55820693634a7256b0b4e4b80"

PV = "2008.10-rc2+${PR}+git${SRCREV}"
PR="r2"

SRC_URI = "git://gitorious.org/u-boot-omap3/mainline.git;branch=master;protocol=git"

UBOOT_MACHINE_beagleboard = "omap3_beagle_config"
UBOOT_MACHINE_omap3evm = "omap3_evm_config"
UBOOT_MACHINE_overo = "omap3_overo_config"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(beagleboard|omap3evm|overo)"
