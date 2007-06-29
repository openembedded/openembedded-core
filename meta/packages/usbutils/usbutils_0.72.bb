DESCRIPTION = "Host side USB console utilities."
SECTION = "base"
DEPENDS += "libusb"
LICENSE = "GPL"
PRIORITY = "optional"
DEFAULT_PREFERENCE = "1"

SRC_URI = "${SOURCEFORGE_MIRROR}/linux-usb/usbutils-${PV}.tar.gz"
S = "${WORKDIR}/usbutils-${PV}"

inherit autotools

EXTRA_OECONF = "--program-prefix="
sbindir = "/sbin"
bindir = "/bin"

FILES_${PN} += "${datadir}/usb*"

do_configure_prepend() {
	rm -rf ${S}/libusb
}
