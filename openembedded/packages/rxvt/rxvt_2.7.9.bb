DESCRIPTION = "Well known terminal emulator"
SECTION = "x11/utils"
PR = "r1"
DEPENDS = "x11 libxft"
LICENSE = "GPL"
SRC_URI = "${HANDHELDS_CVS};module=apps/rxvt \
	   file://include.patch;patch=1"

inherit autotools update-alternatives

ALTERNATIVE_NAME = "x-terminal-emulator"
ALTERNATIVE_PATH = "${bindir}/rxvt-2.7.9"

S = ${WORKDIR}/rxvt

EXTRA_OECONF = "--disable-menubar --disable-xim \
		--enable-utmp --enable-wtmp --enable-lastlog \
		--disable-strings --with-term=rxvt --enable-keepscrolling \
		--with-xft --with-name=rxvt --enable-frills \
		--enable-swapscreen --enable-transparency \
		--with-codesets=eu,jp \
		--enable-cursor-blink --enable-pointer-blank \
		--enable-text-blink --enable-plain-scroll \
		--enable-combining --enable-shared \
		--with-x=${STAGING_LIBDIR}/.."
		
EXTRA_OEMAKE = "'XINC=-I${STAGING_INCDIR}' \
		'XLIB=-L${STAGING_LIBDIR} -lX11 -lXft'"

do_configure () {
	mv autoconf/configure.in . || true
	rm autoconf/libtool.m4
	libtoolize --force
	autotools_do_configure
	echo '#define RXVT_UTMP_FILE "${localstatedir}/run/utmp"' >> config.h
	echo '#define RXVT_WTMP_FILE "${localstatedir}/log/wtmp"' >> config.h
	echo '#define RXVT_LASTLOG_FILE "${localstatedir}/log/lastlog"' >> config.h
	echo '#define HAVE_XLOCALE 1' >> config.h
	echo '#define HAVE_UTMP_H 1' >> config.h
	echo '#define HAVE_TTYSLOT 1' >> config.h
}
