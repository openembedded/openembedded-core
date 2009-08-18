DESCRIPTION = "Task list application"
LICENSE = "GPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ eds-dbus libowl"

inherit autotools_stage pkgconfig gtk-icon-cache

SRC_URI_append_poky = " file://tasks-owl.diff;patch=1 "