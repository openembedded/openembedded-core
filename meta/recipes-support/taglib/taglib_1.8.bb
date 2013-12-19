SUMMARY = "Library for reading and editing the meta-data of popular audio formats"
SECTION = "libs/multimedia"
HOMEPAGE = "http://developer.kde.org/~wheeler/taglib.html"
LICENSE = "LGPLv2.1 | MPL-1"
LIC_FILES_CHKSUM = "file://COPYING.LGPL;md5=4fbd65380cdd255951079008b364516c \
                    file://COPYING.MPL;md5=bfe1f75d606912a4111c90743d6c7325 \
                    file://taglib/audioproperties.h;beginline=1;endline=24;md5=9df2c7399519b7310568a7c55042ecee"

DEPENDS = "zlib"

PR = "r1"
# http://developer.kde.org/~wheeler/files/src/taglib-${PV}.tar.gz
SRC_URI = "https://github.com/downloads/taglib/taglib/taglib-1.8.tar.gz \
           "

SRC_URI[md5sum] = "dcb8bd1b756f2843e18b1fdf3aaeee15"
SRC_URI[sha256sum] = "66d33481703c90236a0a9d1c38fd81b584ca7109ded049225f5463dcaffc209a"

S = "${WORKDIR}/taglib-${PV}"

inherit cmake pkgconfig binconfig

PACKAGES =+ "${PN}-c"
FILES_${PN}-c = "${libdir}/libtag_c.so.*"

EXTRA_OECMAKE = "-DLIB_SUFFIX=${@d.getVar('baselib', True).replace('lib', '')}"

do_configure_prepend () {
	rm -f ${S}/admin/ltmain.sh
	rm -f ${S}/admin/libtool.m4.in
}

