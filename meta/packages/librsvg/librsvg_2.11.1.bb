DESCRIPTION = "Library for rendering SVG files"
SECTION = "x11/utils"
DEPENDS = "gtk+ libcroco libart-lgpl libxml2 popt"
LICENSE = "LGPL"
PR = "r1"

EXTRA_OECONF = "--disable-mozilla-plugin"

inherit autotools pkgconfig gnome

PACKAGES =+ "librsvg-gtk librsvg-gtk-dev rsvg"
FILES_${PN} = "${libdir}/*.so.*"
FILES_rsvg = "${bindir}/rsvg ${bindir}/rsvg-view ${datadir}/pixmaps/svg-viewer.svg"
FILES_librsvg-gtk = "${libdir}/gtk-2.0/*/*/*.so"
FILES_librsvg-gtk-dev = "${libdir}/gtk-2.0"

EXTRA_OECONF = "--disable-mozilla-plugin"

do_stage() {
	autotools_stage_all
}
