DESCRIPTION = "GNOME Accessibility Implementation Library"
SECTION = "x11/libs"

DEPENDS = "gtk+"
PROVIDES = "virtual/gail"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

PR = "r0"

inherit gnome

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/gtk-2.0/modules/*.so"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"

