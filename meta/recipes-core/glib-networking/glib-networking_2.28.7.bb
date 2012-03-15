DESCRIPTION = "glib-networking contains the implementations of certain GLib networking features that cannot be implemented directly in GLib itself because of their dependencies."
HOMEPAGE = "http://git.gnome.org/browse/glib-networking/"
BUGTRACKER = "http://bugzilla.gnome.org"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

SECTION = "libs"
DEPENDS = "glib-2.0 gnutls intltool-native"

PR = "r2"

SRC_URI = "${GNOME_MIRROR}/${BPN}/2.28/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "c10e51571d03c10111a37bcd21fbf777"
SRC_URI[sha256sum] = "98bedfbd530c4b1b53c91025fe82290bafd289d249e4eb549c3b90d23a76021c"

EXTRA_OECONF = "--without-ca-certificates"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/gio/modules/libgio*.so ${datadir}/dbus-1/services/"
FILES_${PN}-dbg += "${libdir}/gio/modules/.debug/"
FILES_${PN}-dev += "${libdir}/gio/modules/libgio*.la"
FILES_${PN}-staticdev += "${libdir}/gio/modules/libgio*.a"
