require u-boot.inc

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=4c6cde5df68eff615d36789dc18edd3b \
                    file://README;beginline=1;endline=22;md5=3a00ef51d3fc96e9d6c1bc4708ccd3b5"

FILESDIR = "${@os.path.dirname(bb.data.getVar('FILE',d,1))}/u-boot-git/${MACHINE}"

SRCREV = "v2010.12"
PV = "v2010.12+${PR}+git${SRCPV}"
PR="r2"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master;protocol=git"

UBOOT_MACHINE_beagleboard = "omap3_beagle_config"
UBOOT_MACHINE_overo = "omap3_overo_config"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"

COMPATIBLE_MACHINE = "(beagleboard|overo)"
