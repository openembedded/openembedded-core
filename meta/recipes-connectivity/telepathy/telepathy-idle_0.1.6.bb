SUMMARY = "Telepathy IRC connection manager"
DESCRIPTION = "Telepathy implementation of the Internet Relay Chat protocols."
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus telepathy-glib openssl"
LICENSE = "LGPL"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/${PN}/${P}.tar.gz"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
