HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus"
LICENSE = "lgpl"

SRC_URI = "http://telepathy.freedesktop.org/releases/libtelepathy/libtelepathy-${PV}.tar.gz"

inherit autotools pkgconfig


FILES_${PN} += "${datadir}/telepathy \
		${datadir}/dbus-1"
