DESCRIPTION = "Dates is a calendar application."
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus libowl"
RDEPENDS = "libedata-cal"

inherit autotools_stage pkgconfig gtk-icon-cache

# Poky/Sato specific enhancements
EXTRA_OECONF_append_poky = " --enable-owl=true --disable-dnd"
SRC_URI_append_poky = " file://dates-owl-window-menu.patch;patch=1 "

