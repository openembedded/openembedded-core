SUMMARY = "Device Tree Compiler"
DESCRIPTION = "The Device Tree Compiler is a tool used to manipulate the Open-Firmware-like device tree used by PowerPC kernels."
SECTION = "bootloader"
PRIORITY = "optional"
LICENSE = "GPL"
DEPENDS = "flex-native bison-native"

inherit autotools

do_install() {
	install -m 0755 -d ${D}${bindir}
	install -m 755 dtc ${D}${bindir}/dtc
}
