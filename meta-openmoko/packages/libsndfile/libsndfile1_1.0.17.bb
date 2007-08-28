DESCRIPTION = "An audio format Conversion library"
HOMEPAGE = "http://www.mega-nerd.com/libsndfile"
AUTHOR = "Erik de Castro Lopo"
SECTION = "libs/multimedia"
LICENSE = "LGPL"
PR = "r0"

SRC_URI = "http://www.mega-nerd.com/libsndfile/libsndfile-${PV}.tar.gz"
S = "${WORKDIR}/libsndfile-${PV}"

inherit autotools lib_package pkgconfig

do_stage() {
	oe_libinstall -a -so -C src libsndfile ${STAGING_LIBDIR}
	install -m 0644 ${S}/src/sndfile.h ${STAGING_INCDIR}/
}

