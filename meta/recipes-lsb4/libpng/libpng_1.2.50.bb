SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=c3d807a85c09ebdff087f18b4969ff96 \
                    file://png.h;beginline=310;endline=424;md5=b87b5e9252a3e14808a27b92912d268d"
DEPENDS = "zlib"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng12/${PV}/libpng-${PV}.tar.xz"

SRC_URI[md5sum] = "a3e00fccbfe356174ab515b5c00641c7"
SRC_URI[sha256sum] = "4724f81f8c92ac7f360ad1fbf173396ea7c535923424db9fbaff07bfd9d8e8e7"

inherit autotools binconfig pkgconfig

PACKAGES =+ "${PN}12"

FILES_${PN}12 = "${libdir}/libpng12${SOLIBS}"
RPROVIDES_${PN}-dev += "${PN}12-dev"
