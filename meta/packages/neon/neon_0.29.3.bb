DESCRIPTION = "neon is an HTTP and WebDAV client library, with a C interface."
HOMEPAGE = "http://www.webdav.org/neon/"
SECTION = "libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/COPYING.LIB;md5=f30a9716ef3762e3467a2f62bf790f0a \
                    file://src/ne_utils.h;beginline=1;endline=20;md5=2caca609538eddaa6f6adf120a218037"
DEPENDS = "zlib libxml2 expat time gnutls"

PR = "r0"

SRC_URI = "http://www.webdav.org/${PN}/${P}.tar.gz \
           file://pkgconfig.patch;patch=1"

inherit autotools binconfig lib_package pkgconfig

EXTRA_OECONF = "--with-ssl=gnutls --with-libxml2 --with-expat --enable-shared"
