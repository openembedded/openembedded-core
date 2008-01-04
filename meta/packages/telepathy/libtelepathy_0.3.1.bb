DESCRIPTION = "Telepathy framework - GLib library"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib"
LICENSE = "LGPL"
PR = "r2"

SRC_URI = "http://telepathy.freedesktop.org/releases/libtelepathy/libtelepathy-${PV}.tar.gz \
           file://prefer_python_2.5.patch;patch=1 "

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
		${datadir}/dbus-1"

do_stage() {
    autotools_stage_all
}
