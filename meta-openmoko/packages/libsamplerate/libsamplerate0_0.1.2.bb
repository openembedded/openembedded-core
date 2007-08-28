DESCRIPTION = "An audio Sample Rate Conversion library"
SECTION = "libs"
LICENSE = "GPL libsamplerate"
PR = "r2"

SRC_URI = "http://www.mega-nerd.com/SRC/libsamplerate-${PV}.tar.gz"
S = "${WORKDIR}/libsamplerate-${PV}"

inherit autotools pkgconfig

do_stage() {
	oe_libinstall -a -so -C src libsamplerate ${STAGING_LIBDIR}
	install -m 0644 ${S}/src/samplerate.h ${STAGING_INCDIR}/
}
