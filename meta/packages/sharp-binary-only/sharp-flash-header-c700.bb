SECTION = "base"
SRC_URI = "file://header-c700.bin"
LICENSE = "sharp-binary-only"
COMPATIBLE_MACHINE = '(c7x0|spitz|akita)'
# Install into machine specific staging area so do_rootfs can find this
PACKAGE_ARCH = "${MACHINE_ARCH}"

do_install() {
	install -d ${D}${libdir}/sharp-flash-header
	install -m 0644 ${WORKDIR}/header-c700.bin ${D}${libdir}/sharp-flash-header/header-c700.bin
}
