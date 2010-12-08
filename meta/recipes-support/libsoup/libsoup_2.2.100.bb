DESCRIPTION = "An HTTP library implementation in C"
LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r1"

DEPENDS = "glib-2.0 gnutls libxml2"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${PN}/2.2/${PN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "936e29d705aab0483b9a5b8860f68c13"
SRC_URI[sha256sum] = "fa9f33e96a11133adbfd10538d95ed9704e582ef334c0a119a2a0bfca302877d"

inherit autotools pkgconfig

FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev = "${includedir}/ ${libdir}/"
FILES_${PN}-doc = "${datadir}/"
