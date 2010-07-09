DESCRIPTION = "An HTTP library implementation in C"
HOMEPAGE = "http://www.gnome.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"

SECTION = "x11/gnome/libs"
PR = "r0"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy sqlite3 libgnome-keyring"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libsoup/2.31/libsoup-${PV}.tar.bz2"
S = "${WORKDIR}/libsoup-${PV}"

inherit autotools pkgconfig

do_stage() {
	autotools_stage_all
}
