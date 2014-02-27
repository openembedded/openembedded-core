SUMMARY = "Telepathy Framework glib-base helper library"
DESCRIPTION = "Telepathy Framework: GLib-based helper library for connection managers"
HOMEPAGE = "http://telepathy.freedesktop.org/wiki/"
DEPENDS = "glib-2.0 dbus python-native-runtime dbus-native dbus-glib libxslt-native"
LICENSE = "LGPLv2.1+"

SRC_URI = "http://telepathy.freedesktop.org/releases/telepathy-glib/${BP}.tar.gz"
SRC_URI[md5sum] = "834ff3b4e2f0e696492a525fe1838f06"
SRC_URI[sha256sum] = "37d5d0eb8cb801822d579182b84a9abdc24e51e5a54b663f865fd1ba2e062c83"

LIC_FILES_CHKSUM = "file://COPYING;md5=e413d83db6ee8f2c8e6055719096a48e"

inherit autotools pkgconfig gettext

FILES_${PN} += "${datadir}/telepathy \
                ${datadir}/dbus-1"
