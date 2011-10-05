DESCRIPTION = "GNOME Accessibility Implementation Library"
SECTION = "x11/libs"

DEPENDS = "gtk+"
PROVIDES = "virtual/gail"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

PR = "r1"

inherit gnome

SRC_URI[archive.md5sum] = "e805806f897cf6040e1f3e9c0cd2151b"
SRC_URI[archive.sha256sum] = "5025b13a4f3b960c8ab7c3e5c7d6d86082e2e1af6ee18e61e37fd4ce3dcc0153"

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/gtk-2.0/modules/*.so"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/modules/.debug"

