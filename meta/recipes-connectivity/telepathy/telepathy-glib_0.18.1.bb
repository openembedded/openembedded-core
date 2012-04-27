SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "461732739e3fbf8467991bbe661fb29a"
SRC_URI[sha256sum] = "3738256689e34c22ddf9c4eb33f44ce7505c18fb1a14c53b8e56dd9c262f4c6d"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

do_install_append() {
	rmdir ${D}${bindir}
	rmdir ${D}${libexecdir}
	rmdir ${D}${servicedir}
}
