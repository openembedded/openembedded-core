DESCRIPTION = "This software provides support for the Tag Image File Format (TIFF)"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=34da3db46fab7501992f9615d7e158cf"
HOMEPAGE = "http://www.remotesensing.org/libtiff/"
DEPENDS = "zlib jpeg xz"
PR = "r1"

SRC_URI = "ftp://ftp.remotesensing.org/pub/libtiff/tiff-${PV}.tar.gz \
           file://libtool2.patch"

SRC_URI[md5sum] = "fae149cc9da35c598d8be897826dfc63"
SRC_URI[sha256sum] = "9a7a039e516c37478038740f1642818250bfb1414cf404cc8b569e5f9d4bf2f0"

inherit autotools

CACHED_CONFIGUREVARS = "ax_cv_check_gl_libgl=no"

PACKAGES =+ "tiffxx tiffxx-dbg tiffxx-dev tiffxx-staticdev tiff-utils tiff-utils-dbg"
FILES_tiffxx = "${libdir}/libtiffxx.so.*"
FILES_tiffxx-dev = "${libdir}/libtiffxx.so ${libdir}/libtiffxx.la"
FILES_tiffxx-staticdev = "${libdir}/libtiffxx.a"
FILES_tiffxx-dbg += "${libdir}/.debug/libtiffxx.so*"
FILES_tiff-utils = "${bindir}/*"
FILES_tiff-utils-dbg += "${bindir}/.debug/"
