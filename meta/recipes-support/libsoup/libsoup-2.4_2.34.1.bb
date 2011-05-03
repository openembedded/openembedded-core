DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

SECTION = "x11/gnome/libs"
PR = "r0"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 libgnome-keyring"

SRC_URI = "${GNOME_MIRROR}/libsoup/2.34/libsoup-${PV}.tar.bz2"

SRC_URI[md5sum] = "846779d898c11f97b5de28a79ea82254"
SRC_URI[sha256sum] = "a2f846af2c4c08e15eacc9879c8c9be4d85f8105f960f96a9ba3dbabd9ee517b"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

