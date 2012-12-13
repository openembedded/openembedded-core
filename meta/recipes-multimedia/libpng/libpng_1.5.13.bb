SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=00b5b35853278d508806c2e5860e82cb \
                    file://png.h;beginline=207;endline=321;md5=50e583fb60bb36f37ab5023b2a3715d1"
DEPENDS = "zlib"
PR = "r0"
LIBV = "15"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz"

SRC_URI[md5sum] = "0b607a685da977f1bfc96e1c47055183"
SRC_URI[sha256sum] = "b843f9cb01d10ae22acd9aaf58aceaa4a6aeb9cf78943b41588004b271257aee"

inherit autotools binconfig pkgconfig

PACKAGES =+ "${PN}${LIBV}"

FILES_${PN}${LIBV} = "${libdir}/libpng${LIBV}${SOLIBS}"
RPROVIDES_${PN}-dev += "${PN}${LIBV}-dev"

BBCLASSEXTEND = "native nativesdk"
