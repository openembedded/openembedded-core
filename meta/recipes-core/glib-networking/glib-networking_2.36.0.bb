DESCRIPTION = "glib-networking contains the implementations of certain GLib networking features that cannot be implemented directly in GLib itself because of their dependencies."
HOMEPAGE = "http://git.gnome.org/browse/glib-networking/"
BUGTRACKER = "http://bugzilla.gnome.org"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2"

SECTION = "libs"
DEPENDS = "glib-2.0 gnutls intltool-native"

SRC_URI = "${GNOME_MIRROR}/${BPN}/2.36/${BPN}-${PV}.tar.xz"

SRC_URI[md5sum] = "062dafdb939510e7b38eea3d3b367be0"
SRC_URI[sha256sum] = "190d66fbaeb023ba4f43c315f23c5372c43be6cbe857596e00990211514650d9"

EXTRA_OECONF = "--without-ca-certificates --without-gnome-proxy --without-libproxy"

inherit autotools pkgconfig

FILES_${PN} += "${libdir}/gio/modules/libgio*.so ${datadir}/dbus-1/services/"
FILES_${PN}-dbg += "${libdir}/gio/modules/.debug/"
FILES_${PN}-dev += "${libdir}/gio/modules/libgio*.la"
FILES_${PN}-staticdev += "${libdir}/gio/modules/libgio*.a"
