SUMMARY = "Collection of simple PIN or passphrase entry dialogs"
DESCRIPTION = "\
	Pinentry is a collection of simple PIN or passphrase entry dialogs which \
	utilize the Assuan protocol as described by the aegypten project; see \
	http://www.gnupg.org/aegypten/ for details."

HOMEPAGE = "http://www.gnupg.org/related_software/pinentry/index.en.html"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=cbbd794e2a0a289b9dfcc9f513d1996e"

inherit autotools

SRC_URI = "ftp://ftp.gnupg.org/gcrypt/${BPN}/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "d224d031130aedb44b05164cb04ed88b"
SRC_URI[sha256sum] = "9cd08e856b395df3adc7124170b53f77c6d5c8bf88e899e818648ec70d3e9695"

EXTRA_OECONF = "--disable-rpath \
		        --disable-dependency-tracking \
               "

PACKAGECONFIG ??= "ncurses libcap"

PACKAGECONFIG[ncurses] = "--enable-ncurses  --with-ncurses-include-dir=${STAGING_INCDIR}, --disable-ncurses, ncurses"
PACKAGECONFIG[libcap] = "--with-libcap, --without-libcap, libcap"
PACKAGECONFIG[qt4] = "--enable-pinentry-qt4, --disable-pinentry-qt4, qt4-x11"
PACKAGECONFIG[gtk2] = "--enable-pinentry-gtk2, --disable-pinentry-gtk2, gtk+ glib-2.0"
