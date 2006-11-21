DESCRIPTION = "Library and test program for decoding mpeg-2 and mpeg-1 video streams"
HOMEPAGE = "http://libmpeg2.sourceforge.net/"
LICENSE = "GPL"
PRIORITY = "optional"
SECTION = "libs"
PR = "r1"

DEPENDS = "virtual/libx11"

SRC_URI = "http://libmpeg2.sourceforge.net/files/mpeg2dec-${PV}.tar.gz"
S = "${WORKDIR}/mpeg2dec-0.4.0"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-shared --disable-sdl --with-x"

PACKAGES = "mpeg2dec mpeg2dec-doc libmpeg2 libmpeg2-dev libmpeg2convert libmpeg2convert-dev"

FILES_${PN} = "${bindir}/*"
FILES_libmpeg2 = "${libdir}/libmpeg2.so.*"
FILES_libmpeg2convert = "${libdir}/libmpeg2convert.so.*"
FILES_libmpeg2-dev = "${libdir}/libmpeg2.so \
                      ${libdir}/libmpeg2.*a \
                      ${libdir}/pkgconfig/libmpeg2.pc \
                      ${includedir}/mpeg2dec/mpeg2.h"
FILES_libmpeg2convert-dev = "${libdir}/libmpeg2convert.so \
                             ${libdir}/libmpeg2convert.*a \
                             ${libdir}/pkgconfig/libmpeg2convert.pc \
                             ${includedir}/mpeg2dec/mpeg2convert.h"

mpeg2dec_include = "mpeg2.h mpeg2convert.h"

do_stage () {
	oe_libinstall -so -C libmpeg2/.libs libmpeg2 ${STAGING_LIBDIR}
	oe_libinstall -so -C libmpeg2/convert/.libs libmpeg2convert ${STAGING_LIBDIR}

	mkdir -p ${STAGING_INCDIR}/mpeg2dec/
	for i in ${mpeg2dec_include}; do
		install -m 0644 include/$i ${STAGING_INCDIR}/mpeg2dec/
	done
}
