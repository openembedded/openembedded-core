DESCRIPTION = "User interface for the OProfile tool"
SECTION = "x11"
LICENSE = "GPLv2"
DEPENDS = "glib-2.0 gtk+ libglade gnome-vfs libxml2"
PR = "r2"

inherit autotools pkgconfig

PACKAGES =+ "oprofileui-server oprofileui-viewer"

FILES_oprofileui-viewer = "${bindir}/oparchconv ${bindir}/oprofile-viewer ${datadir}/applications/ ${datadir}/oprofileui/"
RDEPENDS_oprofile-viewer = "oprofile"

FILES_oprofileui-server = "${bindir}/oprofile-server"
RDEPENDS_oprofile-server = "oprofile"

FILES_oprofileui-doc = "/usr/doc/oprofileui"
