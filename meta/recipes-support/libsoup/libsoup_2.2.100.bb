DESCRIPTION = "An HTTP library implementation in C"
SECTION = "x11/gnome/libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"
PR = "r1"

DEPENDS = "glib-2.0 gnutls libxml2"

SRC_URI = "http://ftp.gnome.org/pub/GNOME/sources/${BPN}/2.2/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "936e29d705aab0483b9a5b8860f68c13"
SRC_URI[sha256sum] = "fa9f33e96a11133adbfd10538d95ed9704e582ef334c0a119a2a0bfca302877d"

inherit autotools pkgconfig

FILES_${PN} = "${libdir}/lib*.so.*"
FILES_${PN}-dev = "${includedir}/ ${libdir}/"
FILES_${PN}-doc = "${datadir}/"
