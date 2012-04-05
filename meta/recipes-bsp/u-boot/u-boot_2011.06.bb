require u-boot.inc

# To build u-boot for your machine, provide the following lines in your machine
# config, replacing the assignments as appropriate for your machine.
# UBOOT_MACHINE = "omap3_beagle_config"
# UBOOT_ENTRYPOINT = "0x80008000"
# UBOOT_LOADADDRESS = "0x80008000"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=1707d6db1d42237583f50183a5651ecb \
                    file://README;beginline=1;endline=22;md5=5ba4218ac89af7846802d0348df3fb90"

FILESDIR = "${@os.path.dirname(d.getVar('FILE',1))}/u-boot-git/${MACHINE}"

# This revision corresponds to the tag "v2011.06"
# We use the revision in order to avoid having to fetch it from the repo during parse
SRCREV = "b1af6f532e0d348b153d5c148369229d24af361a"

PV = "v2011.06+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.denx.de/u-boot.git;branch=master;protocol=git"

S = "${WORKDIR}/git"

PACKAGE_ARCH = "${MACHINE_ARCH}"
