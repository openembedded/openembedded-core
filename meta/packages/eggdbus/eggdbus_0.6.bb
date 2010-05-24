DESCRIPTION = "gobject dbus binding"
HOMEPAGE = "http://cgit.freedesktop.org/~david/eggdbus"
LICENSE = "GPLv2"
PR = "r1"

DEPENDS = "dbus glib-2.0 dbus-glib eggdbus-native"
DEPENDS_virtclass-native = "dbus-native glib-2.0-native dbus-glib-native"

BASE_SRC_URI = "http://cgit.freedesktop.org/~david/${BPN}/snapshot/${BPN}-${PV}.tar.bz2 \
          file://gtk-doc.patch;patch=1 \
          "

SRC_URI = "${BASE_SRC_URI} \
           file://marshal.patch;patch=1 \
          "

SRC_URI_virtclass-native = "${BASE_SRC_URI}"

inherit autotools

EXTRA_OECONF = " --disable-man-pages --disable-gtk-doc-html "

BBCLASSEXTEND = "native"

PARALLEL_MAKE = ""
