SUMMARY = "Device Tree Compiler"
DESCRIPTION = "The Device Tree Compiler is a tool used to manipulate the Open-Firmware-like device tree used by PowerPC kernels."
SECTION = "bootloader"
LICENSE = "GPL2 | BSD"
DEPENDS = "flex-native bison-native"

inherit autotools

do_install() {
	install -m 0755 -d ${D}${bindir}
	install -m 755 dtc ${D}${bindir}/dtc
}
SRC_URI = "git://www.jdl.com/software/dtc.git;protocol=git \
	file://fix_for_compilation_with_gcc_4.6.0.patch"

INC_PR = "r0"

S = "${WORKDIR}/git"
