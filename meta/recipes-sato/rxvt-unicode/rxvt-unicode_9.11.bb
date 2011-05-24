SECTION = "x11/utils"
DEPENDS = "virtual/libx11 libxt libxft"
DESCRIPTION = "rxvt-unicode is a clone of the well known \
terminal emulator rxvt, modified to store text in Unicode \
(either UCS-2 or UCS-4) and to use locale-correct input and \
output. It also supports mixing multiple fonts at the \
same time, including Xft fonts."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552 \
                    file://src/main.C;beginline=1;endline=31;md5=633e23cdeb89fe980ded9a3af4f335c2"
SRC_URI = "http://dist.schmorp.de/rxvt-unicode/Attic/rxvt-unicode-${PV}.tar.bz2 \
	   file://xwc.patch \
	   file://rxvt.desktop \
	   file://rxvt.png"
PR = "r0"

inherit autotools update-alternatives

PROVIDES = "virtual/x-terminal-emulator"
ALTERNATIVE_NAME = "x-terminal-emulator"
ALTERNATIVE_PATH = "${bindir}/rxvt"

CFLAGS_append = " -fpermissive"

# This is necessary so that the "tic" command executed during the install can
# link with the correct libary in staging.
export LD_LIBRARY_PATH = "${STAGING_LIBDIR_NATIVE}"

EXTRA_OECONF = "--enable-menubar --enable-xim \
		--enable-utmp --enable-wtmp --enable-lastlog \
		--disable-strings --with-term=rxvt --enable-keepscrolling \
		--enable-xft --with-name=rxvt --enable-frills \
		--enable-swapscreen --enable-transparency \
		--with-codesets=eu \
		--enable-cursor-blink --enable-pointer-blank \
		--enable-text-blink --enable-rxvt-scroll \
		--enable-combining --enable-shared \
		--enable-xgetdefault \
                --disable-perl \
		--with-x=${STAGING_DIR_HOST}${prefix}"

do_configure_prepend () {
	cp aclocal.m4 acinclude.m4
}

do_compile_prepend () {
	echo '#define UTMP_FILE "${localstatedir}/run/utmp"' >> config.h
	echo '#define WTMP_FILE "${localstatedir}/log/wtmp"' >> config.h
	echo '#define LASTLOG_FILE "${localstatedir}/log/lastlog"' >> config.h
	echo '#define HAVE_XLOCALE 1' >> config.h
}

do_install_append () {
	install -d ${D}/${datadir}
	install -d ${D}/${datadir}/applications
	install -d ${D}/${datadir}/pixmaps/

	install -m 0644 ${WORKDIR}/rxvt.png ${D}/${datadir}/pixmaps
	install -m 0644 ${WORKDIR}/rxvt.desktop ${D}/${datadir}/applications
}

FILES_${PN} += "${datadir}/applications/rxvt.desktop ${datadir}/pixmaps/rxvt.png"

SRC_URI[md5sum] = "a23aa40b31e843878b6f9c44768de430"
SRC_URI[sha256sum] = "1c238f7e545b1a8da81239b826fb2a7d196c73effbcbd211db7a50995a0a067a"
