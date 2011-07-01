DESCRIPTION = "An accessibility toolkit for GNOME."
HOMEPAGE = "http://live.gnome.org/GAP/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2+"

SECTION = "x11/libs"

DEPENDS = "glib-2.0 gtk-doc-native"

inherit autotools pkgconfig

EXTRA_OECONF = "--disable-glibtest"
