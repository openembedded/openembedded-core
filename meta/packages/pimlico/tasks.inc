DESCRIPTION = "Task list application"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ eds-dbus"
DEPENDS_append_poky = " libowl"

inherit autotools_stage pkgconfig gtk-icon-cache

OWL = "--disable-owl"
OWL_poky = "--enable-owl"

EXTRA_OECONF += "${OWL}"
