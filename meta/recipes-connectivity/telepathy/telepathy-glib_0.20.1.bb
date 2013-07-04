SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib libxslt-native"
LICENSE = "LGPLv2.1+"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "780034ff7433f33e6577b02fd7a2cb0a"
SRC_URI[sha256sum] = "01a0343f61fbd389fed9c819751aa75914150aa567472dcb480ced7f4ed261b6"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
