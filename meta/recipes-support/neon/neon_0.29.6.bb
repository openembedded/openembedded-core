DESCRIPTION = "neon is an HTTP and WebDAV client library, with a C interface."
HOMEPAGE = "http://www.webdav.org/neon/"
SECTION = "libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/COPYING.LIB;md5=f30a9716ef3762e3467a2f62bf790f0a \
                    file://src/ne_utils.h;beginline=1;endline=20;md5=2caca609538eddaa6f6adf120a218037"
DEPENDS = "zlib libxml2 expat time gnutls libproxy"
DEPENDS_virtclass-native = "zlib-native libxml2-native expat-native gnutls-native"

PR = "r2"

BBCLASSEXTEND = "native"

SRC_URI = "http://www.webdav.org/${BPN}/${BPN}-${PV}.tar.gz \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "591e0c82e6979e7e615211b386b8f6bc"
SRC_URI[sha256sum] = "9c640b728d6dc80ef1e48f83181166ab6bc95309cece5537e01ffdd01b96eb43"

inherit autotools binconfig lib_package pkgconfig

EXTRA_OECONF = "--with-ssl=gnutls --with-libxml2 --with-expat --enable-shared"
