SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib"
LICENSE = "LGPLv2.1+"
PR = "r0"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BPN}-${PV}.tar.gz"

SRC_URI[md5sum] = "a4a01b748b6b7134853a561228fe70e4"
SRC_URI[sha256sum] = "f2ed6a5207cb579f6807f6f37d4d379a65208c3e094288e57655238b7afd3d66"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"

do_install_append() {
	rmdir ${D}${bindir}
	rmdir ${D}${libexecdir}
	rmdir ${D}${servicedir}
}
