LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r2"
DESCRIPTION = "A powerful object-oriented display"
inherit gnome

DEPENDS = "libglade libart-lgpl"

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/libglade/*/libcanvas.so"
FILES_${PN}-dbg += "${libdir}/libglade/*/.debug/"
FILES_${PN}-dev += "${libdir}/libglade/*/libcanvas.*a"

do_stage() {
	gnome_stage_includes
	oe_libinstall -C libgnomecanvas -a -so libgnomecanvas-2 ${STAGING_LIBDIR}
}
