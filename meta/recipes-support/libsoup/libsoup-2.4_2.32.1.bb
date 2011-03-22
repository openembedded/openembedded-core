DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

SECTION = "x11/gnome/libs"
PR = "r0"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 libgnome-keyring"

SRC_URI = "${GNOME_MIRROR}/libsoup/2.32/libsoup-${PV}.tar.bz2"

SRC_URI[md5sum] = "894ca0077ec2d923286a69766d0b0b74"
SRC_URI[sha256sum] = "8779da1b4d85f443f04ea8301f52c4a1943c10e4dc8d20dbcdb3652921de6b8e"
S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

