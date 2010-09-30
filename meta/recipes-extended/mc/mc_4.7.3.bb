DESCRIPTION = "Midnight Commander is an ncurses based file manager."
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv2"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS = "ncurses-terminfo"

SRC_URI = "http://www.midnight-commander.org/downloads/${PN}-${PV}.tar.bz2"

inherit autotools gettext

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"
