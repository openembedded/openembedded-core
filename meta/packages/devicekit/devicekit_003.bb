DESCRIPTION = "DeviceKit is a simple system service that a) can enumerate devices; b) emits signals when devices are added removed; c) provides a way to merge device information / quirks onto devices."
LICENSE = "GPLv2"
DEPENDS = "udev dbus-glib glib-2.0"

PR = "r1"

SRC_URI = "http://hal.freedesktop.org/releases/DeviceKit-${PV}.tar.gz"
S = "${WORKDIR}/DeviceKit-${PV}"

do_configure_prepend() {
	sed -i -e s:-nonet:\:g ${S}/doc/man/Makefile.am
}	

inherit autotools

FILES_${PN} += "${datadir}/dbus-1/"
