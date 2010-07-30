LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r1"

inherit gnome pkgconfig

FILES_${PN} += "${libdir}/libglade/2.0/*.so"
FILES_${PN}-dev += "${libdir}/libglade/2.0/* ${datadir}/gnome-2.0/ui \
	${libdir}/bonobo-2.0/samples"
FILES_${PN}-dbg += "${libdir}/bonobo-2.0/samples/.debug \
	${libdir}/libglade/2.0/.debug"

DEPENDS = "libgnomecanvas libbonobo libgnome glib-2.0 gtk-doc gconf libxml2 libglade"

EXTRA_OECONF = "--disable-gtk-doc"
