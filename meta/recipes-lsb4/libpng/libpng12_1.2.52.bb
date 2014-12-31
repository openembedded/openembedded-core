SUMMARY = "PNG image format decoding library"
HOMEPAGE = "http://www.libpng.org/"
SECTION = "libs"
LICENSE = "Libpng"
LIC_FILES_CHKSUM = "file://LICENSE;md5=4495b57387451782e5f34c2968c35ed0 \
                    file://png.h;beginline=314;endline=428;md5=0c93e62d39955f2b879c056165cfad43"
DEPENDS = "zlib"

PN = "libpng12"
S = "${WORKDIR}/libpng-${PV}"

SRC_URI = "${SOURCEFORGE_MIRROR}/project/libpng/libpng12/${PV}/libpng-${PV}.tar.xz"

SRC_URI[md5sum] = "49d5c71929bf69a172147c47b9309fbe"
SRC_URI[sha256sum] = "d4fb0fbf14057ad6d0319034188fc2aecddb493da8e3031b7b072ed28f510ec0"

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
