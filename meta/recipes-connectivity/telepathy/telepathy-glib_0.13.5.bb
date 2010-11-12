SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib gettext"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${P}.tar.gz"

LIB_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

