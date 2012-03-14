SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "d436c47689f2709540d849cf5af5ed03"
SRC_URI[sha256sum] = "011ad936f1d592dbc685073244750303fe2201246b16f9a7fa495cf9f8a3fe8d"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

do_install_append() {
	rmdir ${D}${bindir}
	rmdir ${D}${libexecdir}
	rmdir ${D}${servicedir}
}
