DESCRIPTION = "User interface for the OProfile tool"
SECTION = "x11"
LICENSE = "GPLv2"
DEPENDS = "glib-2.0 gtk+ libglade gnome-vfs libxml2 avahi gconf"

inherit autotools pkgconfig gtk-icon-cache

PACKAGES =+ "oprofileui-server oprofileui-viewer"

EXTRA_OECONF = "--with-avahi"

FILES_oprofileui-viewer = "${bindir}/oparchconv ${bindir}/oprofile-viewer ${datadir}/applications/ ${datadir}/oprofileui/"
RDEPENDS_oprofile-viewer = "oprofile"

FILES_oprofileui-server = "${bindir}/oprofile-server"
RDEPENDS_oprofile-server = "oprofile"
