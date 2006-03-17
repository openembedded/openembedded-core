LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r1"
DESCRIPTION = "A powerful object-oriented display"
inherit gnome

DEPENDS = "gnome-vfs libbonobo libglade libart-lgpl"

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/libglade/*/libcanvas.so"

do_stage() {
	gnome_stage_includes
	oe_libinstall -C libgnomecanvas -a -so libgnomecanvas-2 ${STAGING_LIBDIR}
}
