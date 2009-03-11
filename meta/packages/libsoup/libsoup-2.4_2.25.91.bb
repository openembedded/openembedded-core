DESCRIPTION = "An HTTP library implementation in C"
LICENSE = "GPL"
SECTION = "x11/gnome/libs"

DEPENDS = "glib-2.0 gnutls libxml2 libproxy"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/libsoup/2.25/libsoup-${PV}.tar.bz2"
S = "${WORKDIR}/libsoup-${PV}"

inherit autotools_stage pkgconfig

do_stage() {
	autotools_stage_all
}
