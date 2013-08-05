SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib libxslt-native"
LICENSE = "LGPLv2.1+"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz"
SRC_URI[md5sum] = "78be56307d2da7c580cf6df33a225ba0"
SRC_URI[sha256sum] = "c9a307c85d412c58bc68265c4c4128a8e3ffb283bc784143f1f086faaafcb16c"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
