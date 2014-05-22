SUMMARY = "An HTTP and WebDAV client library with a C interface"
HOMEPAGE = "http://www.webdav.org/neon/"
SECTION = "libs"
LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://src/COPYING.LIB;md5=f30a9716ef3762e3467a2f62bf790f0a \
                    file://src/ne_utils.h;beginline=1;endline=20;md5=2caca609538eddaa6f6adf120a218037"
DEPENDS = "zlib libxml2 expat time gnutls libproxy"
DEPENDS_class-native = "zlib-native libxml2-native expat-native gnutls-native"

BBCLASSEXTEND = "native"

SRC_URI = "http://www.webdav.org/${BPN}/${BPN}-${PV}.tar.gz \
           file://pkgconfig.patch"
SRC_URI[md5sum] = "fb60b3a124eeec441937a812c456fd94"
SRC_URI[sha256sum] = "2962cfcb5d30f3272e3d2fa0e473434419770a3801afe3d46e5d1650787990c2"

BINCONFIG = "${bindir}/neon-config"

inherit autotools binconfig-disabled lib_package pkgconfig

EXTRA_OECONF = "--with-ssl=gnutls --with-libxml2 --with-expat --enable-shared"
EXTRA_OECONF += "--without-gssapi"
