DESCRIPTION = "Speex is an Open Source/Free Software patent-free audio compression format designed for speech."
SECTION = "libs"
LICENSE = "BSD"
HOMEPAGE = "http://www.speex.org"
DEPENDS = "libogg"
PR = "r0"

SRC_URI = "http://www.speex.org/download/speex-${PV}.tar.gz"

inherit autotools

# Some interesting options are:
#
#	--enable-arm4-asm
#	--enable-arm5e-asm
#	--enable-fixed-point
#

EXTRA_OECONF_append_openmn = " --enable-arm5e-asm --enable-fixed-point"

do_configure_append() {
	sed -i s/"^OGG_CFLAGS.*$"/"OGG_CFLAGS = "/g Makefile */Makefile */*/Makefile
	sed -i s/"^OGG_LIBS.*$"/"OGG_LIBS = -logg"/g Makefile */Makefile */*/Makefile
	perl -pi -e 's:^includedir.*$:includedir = ${STAGING_INCDIR}:g' Makefile */Makefile */*/Makefile
	perl -pi -e 's:^oldincludedir.*$:includedir = ${STAGING_INCDIR}:g' Makefile */Makefile */*/Makefile
	perl -pi -e 's:\s*-I/usr/include$::g' Makefile */Makefile */*/Makefile
}

do_stage() {
	oe_libinstall -C libspeex/.libs -so libspeex ${STAGING_LIBDIR}
	install -d ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_bits.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_callbacks.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_header.h ${STAGING_INCDIR}/speex
	install -m 0644 include/speex/speex_stereo.h ${STAGING_INCDIR}/speex
}
