DESCRIPTION = "An accessibility toolkit for GNOME."
SECTION = "x11/libs"
PRIORITY = "optional"
LICENSE = "LGPL"

DEPENDS = "glib-2.0 gtk-doc-native"

SRC_URI = "http://download.gnome.org/sources/atk/1.20/${PN}-${PV}.tar.bz2"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-glibtest"

do_stage () {
	autotools_stage_all
}
