DESCRIPTION = "Dates is a calendar application."
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
RDEPENDS = "libedata-cal"

inherit autotools_stage pkgconfig gtk-icon-cache

# Poky/Sato specific enhancements
OWL = "--disable-owl"
OWL_poky = "--enable-owl --disable-dnd"

EXTRA_OECONF += "${OWL}"
DEPENDS_append_poky = " libowl"
SRC_URI_append_poky = " file://dates-owl-window-menu.patch;patch=1 "

