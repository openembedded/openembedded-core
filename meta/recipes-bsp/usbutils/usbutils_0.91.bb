SUMMARY = "Host side USB console utilities."
DESCRIPTION = "Contains the lsusb utility for inspecting the devices connected to the USB bus."
HOMEPAGE = "http://www.linux-usb.org"
SECTION = "base"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "libusb zlib"
PR = "r3"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/usb/usbutils/usbutils-${PV}.tar.gz"

SRC_URI[md5sum] = "49de2403b40bf3a9863faaa8d3858deb"
SRC_URI[sha256sum] = "c122346b0225121bcf159abf804116f826a4a3462c94ce7b8871f7559e6b3a46"

inherit autotools

do_install_append() {
	ln -s ../sbin/lsusb ${D}${bindir}/lsusb
	# We only need the compressed copy, remove the uncompressed version
	rm -f ${D}${datadir}/usb.ids
}

PACKAGES += "${PN}-ids"
FILES_${PN}-dev += "${datadir}/pkgconfig"
FILES_${PN}-ids = "${datadir}/usb*"

RDEPENDS_${PN} = "${PN}-ids bash"
