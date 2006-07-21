DESCRIPTION = "GNU Midnight Commander is a file \
manager for free operating systems."
HOMEPAGE = "http://www.ibiblio.org/mc/"
LICENSE = "GPLv2"
SECTION = "console/utils"
PRIORITY = "optional"
DEPENDS = "ncurses glib-2.0"
RDEPENDS = "ncurses-terminfo"

SRC_URI = "http://www.ibiblio.org/pub/Linux/utils/file/managers/mc/mc-${PV}.tar.gz"

inherit autotools

EXTRA_OECONF = "--disable-glibtest --without-x --without-samba \
--without-nfs --without-gpm-mouse"

do_configure() {
	gnu-configize
	oe_runconf
}

do_install() {
	cd src
	oe_runmake 'DESTDIR=${D}' install
	cd ../syntax
	oe_runmake 'DESTDIR=${D}' install
	cd ../po
	oe_runmake 'DESTDIR=${D}' install
	cd ../vfs
	oe_runmake 'DESTDIR=${D}' install
	cd ..
}
