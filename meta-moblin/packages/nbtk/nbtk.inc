DESCRIPTION = "Moblin toolkit library for netbooks"
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
SRC_URI = "http://download.moblin.org/sources/${PN}/1.2/${PN}-${PV}.tar.bz2"
LICENSE = "LGPLv2.1"
PR = "r4"
DEPENDS = "clutter-1.0 libccss clutter-imcontext"

EXTRA_OECONF = "--disable-introspection"

inherit autotools

do_configure_prepend () {
	echo "EXTRA_DIST=" > ${S}/gtk-doc.make
	touch ${S}/INSTALL
}
