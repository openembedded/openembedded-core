SUMMARY = "PNG Library"
DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=03b8ec701cb796c0ec84af22f884edef \
                    file://png.h;beginline=207;endline=321;md5=e829cebefd08488ba5142ea5faea6821"
DEPENDS = "zlib"
PR = "r0"
LIBV = "16"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng${LIBV}/${PV}/libpng-${PV}.tar.xz \
          "

SRC_URI[md5sum] = "3ee623b9a4d33bda7310a5124080b14d"
SRC_URI[sha256sum] = "5e13c31321083b03956b5ff298bacffab7a7ad35c34c122acef314593944b97b"

inherit autotools binconfig pkgconfig

BBCLASSEXTEND = "native nativesdk"
