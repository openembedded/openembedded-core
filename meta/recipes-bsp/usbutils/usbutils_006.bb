SUMMARY = "Host side USB console utilities."
DESCRIPTION = "Contains the lsusb utility for inspecting the devices connected to the USB bus."
HOMEPAGE = "http://www.linux-usb.org"
SECTION = "base"

LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f"

DEPENDS = "libusb zlib"
PR = "r0"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/usb/usbutils/usbutils-${PV}.tar.gz \
           file://usb-devices-avoid-dependency-on-bash.patch \
           file://Fix-NULL-pointer-crash.patch"

SRC_URI[md5sum] = "850790442f5eb487cdc7e35f8ee03e11"
SRC_URI[sha256sum] = "553270b4030f0844cb70aed57e61c7f10a7ed6641c3bed20249201cec9bcf122"

inherit autotools

do_install_append() {
	# We only need the compressed copy, remove the uncompressed version
	rm -f ${D}${datadir}/usb.ids
}

PACKAGES += "${PN}-ids"
FILES_${PN}-dev += "${datadir}/pkgconfig"
FILES_${PN}-ids = "${datadir}/usb*"

RDEPENDS_${PN} = "${PN}-ids"
