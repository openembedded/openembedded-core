SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "071ed1b34ace45109188df73933609e4"
SRC_URI[sha256sum] = "dae3dd7c06044951e7780cdc3e5524c7c9d12cd8aae6fe42bc88aca39a40b318"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

