DESCRIPTION = "neon is an HTTP and WebDAV client library, with a C interface."
HOMEPAGE = "http://www.webdav.org/neon/"
SECTION = "libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/COPYING.LIB;md5=f30a9716ef3762e3467a2f62bf790f0a \
                    file://src/ne_utils.h;beginline=1;endline=20;md5=2caca609538eddaa6f6adf120a218037"
DEPENDS = "zlib libxml2 expat time gnutls"

PR = "r0"

SRC_URI = "http://www.webdav.org/${BPN}/${BPN}-${PV}.tar.gz \
           file://pkgconfig.patch"

SRC_URI[md5sum] = "ff369e69ef0f0143beb5626164e87ae2"
SRC_URI[sha256sum] = "dea3c52b9f3f915651c6279c76758acf7c427727e8b9b0130f8b138f24012290"

inherit autotools binconfig lib_package pkgconfig

EXTRA_OECONF = "--with-ssl=gnutls --with-libxml2 --with-expat --enable-shared"
