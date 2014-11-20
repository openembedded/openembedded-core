SUMMARY = "Device Tree Compiler"
DESCRIPTION = "The Device Tree Compiler is a tool used to manipulate the Open-Firmware-like device tree used by PowerPC kernels."
SECTION = "bootloader"
LICENSE = "GPLv2 | BSD"
DEPENDS = "flex-native bison-native"

inherit autotools-brokensep

SRC_URI = "git://git.kernel.org/pub/scm/utils/dtc/dtc.git \
           file://make_install.patch \
	  "

EXTRA_OEMAKE='PREFIX="${prefix}" LIBDIR="${libdir}"'

S = "${WORKDIR}/git"

PACKAGES =+ "${PN}-misc"
FILES_${PN}-misc = "${bindir}/convert-dtsv0 ${bindir}/ftdump ${bindir}/dtdiff"

RDEPENDS_${PN}-misc += "bash"
