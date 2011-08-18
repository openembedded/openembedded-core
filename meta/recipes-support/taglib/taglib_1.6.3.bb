DESCRIPTION = "TagLib is a library for reading and editing the meta-data of several popular audio formats"
SECTION = "libs/multimedia"
HOMEPAGE = "http://developer.kde.org/~wheeler/taglib.html"
LICENSE = "LGPLv2.1 | MPL-1"
LIC_FILES_CHKSUM = "file://COPYING.LGPL;md5=db979804f025cf55aabec7129cb671ed \
                    file://COPYING.MPL;md5=bfe1f75d606912a4111c90743d6c7325 \
                    file://taglib/audioproperties.h;beginline=1;endline=24;md5=39dcdd772f378a0f8c9a41bc929eccce"

PR = "r1"
# http://developer.kde.org/~wheeler/files/src/taglib-${PV}.tar.gz
SRC_URI = "http://launchpad.net/${BPN}/trunk/${PV}/+download/${BPN}-${PV}.tar.gz \
           file://configuretweak.patch \
           file://nolibtool.patch"

SRC_URI[md5sum] = "ddf02f4e1d2dc30f76734df806e613eb"
SRC_URI[sha256sum] = "a9ba089cc2c6d26d266bad492de31cadaeb878dea858e22ae3196091718f284b"

S = "${WORKDIR}/taglib-${PV}"

inherit autotools pkgconfig binconfig

PACKAGES =+ "${PN}-c"
FILES_${PN}-dbg += "${bindir}/taglib-config"
FILES_${PN}-c = "${libdir}/libtag_c.so.*"

do_configure_prepend () {
	rm -f ${S}/admin/ltmain.sh
	rm -f ${S}/admin/libtool.m4.in
}

