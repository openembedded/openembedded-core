DESCRIPTION = "Midnight Commander is an ncurses based file manager."
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS_${PN} = "ncurses-terminfo"

PR = "r2"

SRC_URI = "http://www.midnight-commander.org/downloads/${BPN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "bdae966244496cd4f6d282d80c9cf3c6"
SRC_URI[sha256sum] = "a68338862bb30017eb65ed569a58e80ab66ae8cef11c886440c9e9f4d1efc6ab"

inherit autotools gettext

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"

FILES_${PN}-dbg += "${libexecdir}/mc/.debug/"

do_install_append () {
       sed -i -e '1s,#!.*perl,#!${bindir}/env perl,' ${D}${libexecdir}/mc/extfs.d/*
       
}
