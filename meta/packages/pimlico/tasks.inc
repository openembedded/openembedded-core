DESCRIPTION = "Task list application"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ eds-dbus libowl"
DEPENDS_append_poky = " libowl"

inherit autotools_stage pkgconfig gtk-icon-cache
