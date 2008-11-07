DESCRIPTION = "Linux Bluetooth Stack Userland."
SECTION = "libs"
PRIORITY = "optional"
HOMEPAGE = "http://www.bluez.org"
LICENSE = "GPL"
PR = "r0"

SRC_URI = "http://www.kernel.org/pub/linux/bluetooth/bluez-${PV}.tar.gz"
S = "${WORKDIR}/bluez-${PV}"

inherit autotools pkgconfig
