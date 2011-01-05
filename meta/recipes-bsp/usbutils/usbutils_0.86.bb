SUMMARY = "Host side USB console utilities."
DESCRIPTION = "Contains the lsusb utility for inspecting the devices connected to the USB bus."
HOMEPAGE = "http://www.linux-usb.org"
SECTION = "base"
PRIORITY = "optional"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "libusb zlib"
RDEPENDS_${PN} = "${PN}-ids"
PR = "r1"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-usb/usbutils-${PV}.tar.gz"

SRC_URI[md5sum] = "34979f675d2bcb3e1b45012fa830a53f"
SRC_URI[sha256sum] = "b3b2bea6d2cd87660c8201a47071bf2a9889d8ed90c7203cc768b597799c12f4"

inherit autotools

do_install_append() {
	ln -s ../sbin/lsusb ${D}${bindir}/lsusb
	# We only need the compressed copy, remove the uncompressed version
	rm -f ${D}${datadir}/usb.ids
}

PACKAGES += "${PN}-ids"
FILES_${PN} += "${datadir}/pkgconfig"
FILES_${PN}-ids = "${datadir}/usb*"
