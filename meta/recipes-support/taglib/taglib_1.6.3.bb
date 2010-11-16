DESCRIPTION = "TagLib is a library for reading and editing the meta-data of several popular audio formats"
SECTION = "libs/multimedia"
HOMEPAGE = "http://developer.kde.org/~wheeler/taglib.html"
LICENSE = "LGPLv2.1 | MPLv1.1"
LIC_FILES_CHKSUM = "file://COPYING.LGPL;md5=db979804f025cf55aabec7129cb671ed \
                    file://COPYING.MPL;md5=bfe1f75d606912a4111c90743d6c7325 \
                    file://taglib/audioproperties.h;beginline=1;endline=24;md5=39dcdd772f378a0f8c9a41bc929eccce"

PR = "r0"

SRC_URI = "http://developer.kde.org/~wheeler/files/src/taglib-${PV}.tar.gz \
           file://configuretweak.patch;patch=1 \
           file://nolibtool.patch;patch=1"

S = "${WORKDIR}/taglib-${PV}"

inherit autotools pkgconfig binconfig

PACKAGES =+ "${PN}-c"
FILES_${PN}-dbg += "${bindir}/taglib-config"
FILES_${PN}-c = "${libdir}/libtag_c.so.*"

do_configure_prepend () {
	rm -f ${S}/admin/ltmain.sh
	rm -f ${S}/admin/libtool.m4.in
}

