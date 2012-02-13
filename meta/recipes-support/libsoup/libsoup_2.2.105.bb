DESCRIPTION = "An HTTP library implementation in C"
SECTION = "x11/gnome/libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=55ca817ccb7d5b5b66355690e9abc605"
PR = "r3"

DEPENDS = "glib-2.0 gnutls libxml2"

SRC_URI = "${GNOME_MIRROR}/${BPN}/2.2/${BPN}-${PV}.tar.bz2 \
	    file://dprintf_conflict_with_eglibc.patch"

SRC_URI[md5sum] = "7fa48b06a0e2b0ff3d2fa45cf331f169"
SRC_URI[sha256sum] = "3760a127ee810cfd0fda257ff615d19a2dd8aeece199dad0d18690446df72e8f"

inherit autotools pkgconfig

#FILES_${PN} = "${libdir}/lib*.so.*"
#FILES_${PN}-dev = "${includedir}/ ${libdir}/"
#FILES_${PN}-doc = "${datadir}/"
