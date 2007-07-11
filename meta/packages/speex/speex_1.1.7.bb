DESCRIPTION = "Speex is an Open Source/Free Software patent-free audio compression format designed for speech."
SECTION = "libs"
LICENSE = "BSD"
HOMEPAGE = "http://www.speex.org"
DEPENDS = "libogg"
PR = "r1"

SRC_URI = "http://downloads.us.xiph.org/releases/speex/${PN}-${PV}.tar.gz"

inherit autotools

# Some interesting options are:
#
#	--enable-arm4-asm
#	--enable-arm5e-asm
#	--enable-fixed-point
#
# NB: the arm assembly is currently broken :(
#

EXTRA_OECONF = " --enable-fixed-point --with-ogg-libraries=${STAGING_LIBDIR} --with-ogg-includes=${STAGING_INCDIR}"

do_stage() {
	oe_libinstall -C libspeex/.libs -so libspeex ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_bits.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_callbacks.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_header.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_stereo.h ${STAGING_INCDIR}/speex
}
