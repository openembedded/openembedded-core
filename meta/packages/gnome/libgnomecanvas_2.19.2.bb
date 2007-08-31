LICENSE = "GPL"
SECTION = "x11/gnome/libs"
DESCRIPTION = "A powerful object-oriented display"

inherit gnome

DEPENDS = "libglade libart-lgpl gail"

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/libglade/*/libcanvas.so"
FILES_${PN}-dbg += "${libdir}/libglade/*/.debug/"
FILES_${PN}-dev += "${libdir}/libglade/*/libcanvas.*a"

do_stage() {
	autotools_stage_all
}
