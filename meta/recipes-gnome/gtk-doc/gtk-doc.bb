SUMMARY = "API documentation generation tool for GTK+ and GNOME"
DESCRIPTION = "gtk-doc is a tool for generating API reference documentation. \
It is used for generating the documentation for GTK+, GLib \
and GNOME."
SECTION = "x11/base"
SRC_URI = "file://gtk-doc.m4"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://../gtk-doc.m4;endline=10;md5=868a952d8bb2d825d724854cfaf8f14e"

PR = "r4"

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native"

do_install () {
	install -d ${D}${datadir}/aclocal/
	install -m 0644 ${WORKDIR}/gtk-doc.m4 ${D}${datadir}/aclocal/
}
