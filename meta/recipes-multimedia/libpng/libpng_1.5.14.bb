SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=58c8238139ee86082f8d29eb89304241 \
                    file://png.h;beginline=207;endline=321;md5=4f17771edaee8bb3c17a95d7faaa3681"
DEPENDS = "zlib"
PR = "r0"
LIBV = "15"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
          "

SRC_URI[md5sum] = "94284b01d62ca43c5eb3f6702db08ed8"
SRC_URI[sha256sum] = "1459488e1b58d3be1c798453cf40c522c05bd66b61156cd51f469f17dff87b54"

inherit autotools binconfig pkgconfig

BBCLASSEXTEND = "native nativesdk"
