SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "a2b7245057396318867af7925e927998"
SRC_URI[sha256sum] = "d9d1e48caf36aad0863e110230f10257e8dd53879282588d5ff720ad2bff3c80"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

do_install_append() {
	rmdir ${D}${bindir}
	rmdir ${D}${libexecdir}
	rmdir ${D}${servicedir}
}
