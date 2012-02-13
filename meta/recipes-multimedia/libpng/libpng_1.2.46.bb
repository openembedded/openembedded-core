SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=21b4b6e3523afa9f03f00b43b991dad0 \
                    file://png.h;startline=172;endline=261;md5=996460063a9bf2de35b2d61d2776dabc"
DEPENDS = "zlib"
PR = "r4"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng12/${PV}/libpng-${PV}.tar.bz2"

SRC_URI[md5sum] = "e8b43dc78ef95b3949af7f961d76874b"
SRC_URI[sha256sum] = "a5e796e1802b2e221498bda09ff9850bc7ec9068b6788948cc2c42af213914d8"

inherit autotools binconfig pkgconfig

PACKAGES =+ "${PN}12"

FILES_${PN}12 = "${libdir}/libpng12${SOLIBS}"
FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-dev += " ${bindir} ${sbindir}"
RPROVIDES_${PN}-dev += "${PN}12-dev"

BBCLASSEXTEND = "native"
