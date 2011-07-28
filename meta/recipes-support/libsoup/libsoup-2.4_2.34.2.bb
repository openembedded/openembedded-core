DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

SECTION = "x11/gnome/libs"
PR = "r0"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 libgnome-keyring"

SRC_URI = "${GNOME_MIRROR}/libsoup/2.34/libsoup-${PV}.tar.bz2"

SRC_URI[md5sum] = "2454b38681a6e082b613a781a501e721"
SRC_URI[sha256sum] = "1d70edc48c309528635012269733739f9cd22548913125864318a65d1b6f1261"

S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

