DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib"
LICENSE = "LGPL"
PR = "r1"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${P}.tar.gz \
           file://prefer_python_2.5.patch;patch=1 "

inherit autotools_stage pkgconfig

FILES_${PN} += "${datadir}/telepathy \
		${datadir}/dbus-1"
