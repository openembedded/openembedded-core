DESCRIPTION = "Library and test program for decoding mpeg-2 and mpeg-1 video streams"
HOMEPAGE = "http://libmpeg2.sourceforge.net/"
SECTION = "libs"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://include/mpeg2.h;beginline=1;endline=22;md5=ead62602d4638329d3b5b86a55803154"

DEPENDS = "virtual/libx11"

PR = "r1"

SRC_URI = "http://libmpeg2.sourceforge.net/files/mpeg2dec-${PV}.tar.gz \
           file://altivec_h_needed.patch"

inherit autotools pkgconfig

EXTRA_OECONF = "--enable-shared --disable-sdl --with-x"

PACKAGES = "mpeg2dec-dbg mpeg2dec mpeg2dec-doc libmpeg2 libmpeg2-dev libmpeg2convert libmpeg2convert-dev"

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
