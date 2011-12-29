SECTION = "x11/utils"
DESCRIPTION = "rxvt-unicode is a clone of the well known \
terminal emulator rxvt, modified to store text in Unicode \
(either UCS-2 or UCS-4) and to use locale-correct input and \
output. It also supports mixing multiple fonts at the \
same time, including Xft fonts."
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=b234ee4d69f5fce4486a80fdaf4a4263 \
                    file://src/main.C;beginline=1;endline=31;md5=775485398a09fa7aee6f90464af88432"

DEPENDS = "virtual/libx11 libxt libxft gdk-pixbuf"

PR = "r0"

SRC_URI = "http://dist.schmorp.de/rxvt-unicode/Attic/rxvt-unicode-${PV}.tar.bz2 \
	   file://xwc.patch \
	   file://rxvt.desktop \
	   file://rxvt.png"

SRC_URI[md5sum] = "022235a9a8b012c29e72c49177be6ce4"
SRC_URI[sha256sum] = "29928a33be6b3fb0edd126df2c3d35e1900f8268b546ce54430780c550fa1d52"

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
