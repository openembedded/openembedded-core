DESCRIPTION = "This software provides support for the Tag Image File Format (TIFF)"
LICENSE = "BSD-2-Clause"
LIC_FILES_CHKSUM = "file://COPYRIGHT;md5=34da3db46fab7501992f9615d7e158cf"
HOMEPAGE = "http://www.remotesensing.org/libtiff/"
DEPENDS = "zlib jpeg xz"
PR = "r0"

SRC_URI = "ftp://ftp.remotesensing.org/pub/libtiff/tiff-${PV}.tar.gz \
           file://libtool2.patch"

SRC_URI[md5sum] = "04a08fa1e07e696e820a0c3f32465a13"
SRC_URI[sha256sum] = "aa29f1f5bfe3f443c3eb4dac472ebde15adc8ff0464b83376f35e3b2fef935da"

inherit autotools

CACHED_CONFIGUREVARS = "ax_cv_check_gl_libgl=no"

PACKAGES =+ "tiffxx tiffxx-dbg tiffxx-dev tiffxx-staticdev tiff-utils tiff-utils-dbg"
FILES_tiffxx = "${libdir}/libtiffxx.so.*"
FILES_tiffxx-dev = "${libdir}/libtiffxx.so ${libdir}/libtiffxx.la"
FILES_tiffxx-staticdev = "${libdir}/libtiffxx.a"
FILES_tiffxx-dbg += "${libdir}/.debug/libtiffxx.so*"
FILES_tiff-utils = "${bindir}/*"
FILES_tiff-utils-dbg += "${bindir}/.debug/"

BBCLASSEXTEND = "native"
