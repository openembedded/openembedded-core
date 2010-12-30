DESCRIPTION = "Midnight Commander is an ncurses based file manager."
HOMEPAGE = "http://www.midnight-commander.org/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=59530bdf33659b29e73d4adb9f9f6552"
SECTION = "console/utils"
DEPENDS = "ncurses glib-2.0"
RDEPENDS_${PN} = "ncurses-terminfo"

PR = "r1"

PR = "r0"

SRC_URI = "http://www.midnight-commander.org/downloads/${PN}-${PV}.tar.bz2"

SRC_URI[md5sum] = "4eb2438b168fb0f93b748889a9294f54"
SRC_URI[sha256sum] = "0d2b4e87b8a4158edf54380df9402b4a1a19f7494ef06dd0a0a3e3ff6a2b50f1"

inherit autotools gettext

EXTRA_OECONF = "--with-screen=ncurses --without-gpm-mouse --without-x --without-samba"
