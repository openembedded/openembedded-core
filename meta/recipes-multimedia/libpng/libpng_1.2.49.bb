SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=20110633230abd47fe8680afa75f1492 \
                    file://png.h;startline=308;endline=422;md5=edd1c552386a8c3773d90e278ae30891"
DEPENDS = "zlib"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng12/${PV}/libpng-${PV}.tar.bz2"

SRC_URI[md5sum] = "d5106b70b4f8b464a7da66bffe4565fb"
SRC_URI[sha256sum] = "fbf8faa70ebca2ed2ee6df6f2249f4722517b581af5b6c3c71bbdaf925d5954e"

inherit autotools binconfig pkgconfig

PACKAGES =+ "${PN}12"

FILES_${PN}12 = "${libdir}/libpng12${SOLIBS}"
FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-dev += " ${bindir} ${sbindir}"
RPROVIDES_${PN}-dev += "${PN}12-dev"

BBCLASSEXTEND = "native"
