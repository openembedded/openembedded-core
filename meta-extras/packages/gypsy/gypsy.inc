DESCRIPTION = "GPS Controlling Daemon"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 dbus bluez-libs"

inherit autotools pkgconfig

EXTRA_OECONF += "--with-distro=debian"

do_configure_prepend () {
	touch ${S}/gtk-doc.make
}

FILES_${PN} += "/usr/share/dbus-1/services/"
