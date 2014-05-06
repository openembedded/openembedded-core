SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=ec135c8490e3b3d4ba2cc21f84c3a294 \
                    file://png.h;beginline=314;endline=428;md5=151cf86c4efc2a7400f8a9a61d743d8e"
DEPENDS = "zlib"

PN = "libpng12"
S = "${WORKDIR}/libpng-${PV}"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng12/${PV}/libpng-${PV}.tar.xz"

SRC_URI[md5sum] = "4efba67fa5aa2b785c6fcec2cc3e90c9"
SRC_URI[sha256sum] = "c7d7b813b022afd70474f78bcc3655c7bb54edbf28dd4652e5521cbb6da56d4a"

BINCONFIG_GLOB = "${PN}-config"

inherit autotools binconfig pkgconfig

do_install_append() {
	# The follow link files link to corresponding png12*.h and libpng12* files
	# They conflict with higher verison, so drop them
	unlink ${D}/${includedir}/png.h
	unlink ${D}/${includedir}/pngconf.h

	unlink ${D}/${libdir}/libpng.la
	unlink ${D}/${libdir}/libpng.so
	unlink ${D}/${libdir}/libpng.a
	unlink ${D}/${libdir}/pkgconfig/libpng.pc

	unlink ${D}/${bindir}/libpng-config
}
