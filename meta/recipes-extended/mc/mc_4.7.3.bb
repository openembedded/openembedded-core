DESCRIPTION = "Midnight Commander is an ncurses based file manager."
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS_${PN} = "ncurses-terminfo"

PR = "r1"

SRC_URI = "http://www.midnight-commander.org/downloads/${PN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "42eb806d733b11d0d13ff7ee5fd1a03c"
SRC_URI[sha256sum] = "4815184f28218a43080e4a425b5cd4e159277a10c56ff06d29bf441828fe5927"

inherit autotools gettext

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"
