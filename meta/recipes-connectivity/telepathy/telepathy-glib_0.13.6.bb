SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib gettext"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${P}.tar.gz"

SRC_URI[md5sum] = "db997b1575e9186887caf211c0b94dd9"
SRC_URI[sha256sum] = "76a38a64d1b7fbaa6e1aaf6a9f8b898840c551aea5d9121bdcb35fdbb66c5877"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

