LICENSE = "LGPL"
SECTION = "x11/libs"
PR = "r1"
DESCRIPTION = "GNOME Accessibility Implementation Library"
DEPENDS = "gtk+"
PROVIDES = "virtual/gail"

inherit gnome

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/gtk-2.0/modules/*.so"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"

