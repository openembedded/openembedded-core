SUMMARY = "Host side USB console utilities."
DESCRIPTION = "Contains the lsusb utility for inspecting the devices connected to the USB bus."
HOMEPAGE = "http://www.linux-usb.org"
SECTION = "base"
PRIORITY = "optional"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "libusb zlib"
RDEPENDS_${PN} = "${PN}-ids"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-usb/usbutils-${PV}.tar.gz"

inherit autotools

do_install_append() {
	ln -s ../sbin/lsusb ${D}${bindir}/lsusb
}

PACKAGES += "${PN}-ids"

FILES_${PN} += "${datadir}/pkgconfig"
FILES_${PN}-ids = "${datadir}/usb*"
