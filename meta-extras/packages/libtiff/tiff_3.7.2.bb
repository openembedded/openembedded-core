DESCRIPTION = "This software provides support for the Tag Image File Format (TIFF)"
LICENSE = ""
HOMEPAGE = "http://www.remotesensing.org/libtiff/"
DEPENDS = "zlib jpeg lzo"
PR = "r4"

SRC_URI = "http://dl.maptools.org/dl/libtiff/old/tiff-${PV}.tar.gz \
	  file://configure.patch;patch=1"

inherit autotools

do_stage() {
	autotools_stage_includes
	install -d ${STAGING_LIBDIR}
	oe_libinstall -C libtiff -a -so libtiff ${STAGING_LIBDIR}
	oe_libinstall -C libtiff -a -so libtiffxx ${STAGING_LIBDIR}

}

PACKAGES =+ "tiffxx tiffxx-dbg tiffxx-dev tiff-utils tiff-utils-dbg"
FILES_tiffxx = "${libdir}/libtiffxx.so.*"
FILES_tiffxx-dev = "${libdir}/libtiffxx.so ${libdir}/libtiffxx.*a"
FILES_tiffxx-dbg += "${libdir}/.debug/libtiffxx.so*"
FILES_tiff-utils = "${bindir}/*"
FILES_tiff-utils-dbg += "${bindir}/.debug/"
