DESCRIPTION = "GNOME User Interface Library"
LICENSE = "GPL"
SECTION = "x11/gnome/libs"
DEPENDS = "libgnome libgnomecanvas gnome-keyring libbonoboui"

inherit gnome 

FILES_${PN} += "${libdir}/gtk-2.0/*/filesystems/lib*.so \
                ${libdir}/libglade/*/lib*.so \
                ${datadir}/pixmaps/gnome-about-logo.png"
FILES_${PN}-dev += "${libdir}/gtk-2.0/*/filesystems/*.la ${libdir}/gtk-2.0/*/filesystems/*.a ${libdir}/libglade/*/*.la ${libdir}/libglade/*/*.a"
FILES_${PN}-dbg += "${libdir}/gtk-2.0/*/filesystems/.debug/ ${libdir}/libglade/*/.debug/"

SRC_URI += "file://gnome-stock-pixbufs.h file://no-pixbuf-csource.patch;patch=1"

EXTRA_OECONF = "--disable-gtk-doc"

do_configure_prepend() {
	install -m 0644 ${WORKDIR}/gnome-stock-pixbufs.h ${S}/libgnomeui/pixmaps/gnome-stock-pixbufs.h
}
