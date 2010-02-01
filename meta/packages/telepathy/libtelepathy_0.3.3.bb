DESCRIPTION = "Telepathy framework - GLib library"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus dbus-glib telepathy-glib"
LICENSE = "LGPL"
PR = "r3"

SRC_URI = "http://telepathy.freedesktop.org/releases/libtelepathy/libtelepathy-${PV}.tar.gz \
           file://prefer_python_2.5.patch;patch=1 \
           file://doublefix.patch;patch=1"

inherit autotools_stage pkgconfig

FILES_${PN} += "${datadir}/telepathy \
		${datadir}/dbus-1"
