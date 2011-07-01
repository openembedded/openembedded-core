DESCRIPTION = "PNG Library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=a294a2bb08b7f25558119edbfd6b2e92 \
                    file://png.h;startline=172;endline=261;md5=3253923f0093658f470e52a06ddcf4e7"
DEPENDS = "zlib"
PR = "r0"

SRC_URI = "${SOURCEFORGE_MIRROR}/libpng/libpng-${PV}.tar.bz2"

SRC_URI[md5sum] = "e3ac7879d62ad166a6f0c7441390d12b"
SRC_URI[sha256sum] = "b9ab20f1c2c3bf6c4448fd9bd8a4a8905b918114d5fada56c97bb758a17b7215"

inherit autotools binconfig pkgconfig

PACKAGES =+ "${PN}12-dbg ${PN}12 ${PN}12-dev"

FILES_${PN}12-dbg = "${libdir}/.debug/libpng12*"
FILES_${PN}12 = "${libdir}/libpng12${SOLIBS}"
FILES_${PN}12-dev = "${libdir}/libpng12.* ${includedir}/libpng12 ${libdir}/pkgconfig/libpng12.pc"
FILES_${PN} = "${libdir}/lib*${SOLIBS}"
FILES_${PN}-dev += " ${bindir} ${sbindir}"

BBCLASSEXTEND = "native"
