SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=6150ae2da16b673fa759402d5fb7ef73 \
                    file://png.h;beginline=19;endline=109;md5=96ec141aee7c75a15d77dbe249a71f93"
DEPENDS = "zlib"

PN = "libpng12"
S = "${WORKDIR}/libpng-${PV}"

SRC_URI = "${GENTOO_MIRROR}/libpng-${PV}.tar.xz"

SRC_URI[md5sum] = "68ac486976e73fd164d54740bece57a2"
SRC_URI[sha256sum] = "5e5227345676fabbba28558f4396514bb06a239eaf69adba12f3669a1650797e"

UPSTREAM_CHECK_URI = "http://sourceforge.net/projects/libpng/files/libpng12/"
UPSTREAM_CHECK_REGEX = "/libpng12/(?P<pver>(\d+[\.\-_]*)+)/"

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
