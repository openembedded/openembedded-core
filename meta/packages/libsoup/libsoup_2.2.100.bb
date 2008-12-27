DESCRIPTION = "An HTTP library implementation in C"
LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r1"

DEPENDS = "glib-2.0 gnutls libxml2"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${PN}/2.2/${PN}-${PV}.tar.bz2"

inherit autotools_stage pkgconfig

FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev = "${includedir}/ ${libdir}/"
FILES_${PN}-doc = "${datadir}/"
