DESCRIPTION = "Midnight Commander is an ncurses based file manager."
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://COPYING;md5=270bbafe360e73f9840bd7981621f9c2"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS_${PN} = "ncurses-terminfo"

PR = "r3"

SRC_URI = "http://www.midnight-commander.org/downloads/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "7d60c87d87b359831d976fa28034935e"
SRC_URI[sha256sum] = "8f9bd09750c65a7786197a8f7e5a10a6dabf8c8737c564079ca393d8ecaf944d"

inherit autotools gettext

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"

FILES_${PN}-dbg += "${libexecdir}/mc/.debug/"

do_install_append () {
	sed -i -e '1s,#!.*perl,#!${bindir}/env perl,' ${D}${libexecdir}/mc/extfs.d/*
	rm -rf ${D}${libdir}
}
