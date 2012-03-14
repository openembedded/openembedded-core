DESCRIPTION = "Library for rendering SVG files"
HOMEPAGE = "http://ftp.gnome.org/pub/GNOME/sources/librsvg/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://rsvg.h;beginline=3;endline=24;md5=20b4113c4909bbf0d67e006778302bc6"

SECTION = "x11/utils"
DEPENDS = "gtk+ libcroco cairo libxml2 popt"

PR = "r5"

inherit autotools pkgconfig gnome

EXTRA_OECONF = "--disable-mozilla-plugin --without-svgz --without-croco --disable-gnome-vfs"

SRC_URI += "file://doc_Makefile.patch"

SRC_URI[archive.md5sum] = "4b00d0fee130c936644892c152f42db7"
SRC_URI[archive.sha256sum] = "91b98051f352fab8a6257688d6b2fd665b4648ed66144861f2f853ccf876d334"

do_configure_prepend () {
	export GDK_PIXBUF_QUERYLOADERS="${libdir}/gtk-2.0/version/loaders"
	echo "CLEANFILES=" > gtk-doc.make
}

do_install_append () {
	rmdir ${D}${libdir}/gtk-3.0/engines/
	rmdir ${D}${libdir}/gtk-3.0/
}

PACKAGES =+ "librsvg-gtk librsvg-gtk-dbg librsvg-gtk-dev rsvg"
FILES_${PN} = "${libdir}/*.so.*"
FILES_${PN}-staticdev += "${libdir}/gdk-pixbuf-2.0/*.a ${libdir}/gdk-pixbuf-2.0/*/*/*.a \
                          ${libdir}/gtk-2.0/*.a ${libdir}/gtk-2.0/*/*/*.a"
FILES_rsvg = "${bindir}/rsvg \
	      ${bindir}/rsvg-view \
	      ${bindir}/rsvg-convert \
	      ${datadir}/pixmaps/svg-viewer.svg \
	      ${datadir}/themes"
FILES_librsvg-gtk = "${libdir}/gtk-2.0/*/*/*.so ${libdir}/gdk-pixbuf-2.0/*/*/*.so"
FILES_librsvg-gtk-dev += "${libdir}/gtk-2.0/*.la \
			  ${libdir}/gtk-2.0/*/*/*.la \
			  ${libdir}/gdk-pixbuf-2.0/*.la \
			  ${libdir}/gdk-pixbuf-2.0/*/*/*.la"
FILES_librsvg-gtk-dbg += "${libdir}/gdk-pixbuf-2.0/.debug \
                          ${libdir}/gdk-pixbuf-2.0/*/*/.debug \
                          ${libdir}/gtk-2.0/.debug \
                          ${libdir}/gtk-2.0/*/*/.debug"

pkg_postinst_librsvg-gtk() {
if [ "x$D" != "x" ]; then
  exit 1
fi

if [ -d ${libdir}/gtk-2.0/2.10.0/loaders ] ; then
	export GDK_PIXBUF_MODULEDIR=${libdir}/gtk-2.0/2.10.0/loaders
else
	export GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/2.10.0/loaders
fi

test -x ${bindir}/gdk-pixbuf-query-loaders && gdk-pixbuf-query-loaders > ${sysconfdir}/gtk-2.0/gdk-pixbuf.loaders
test -x ${bindir}/gtk-update-icon-cache && gtk-update-icon-cache  -q ${datadir}/icons/hicolor
}
