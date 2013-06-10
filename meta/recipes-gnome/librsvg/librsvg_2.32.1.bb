DESCRIPTION = "Library for rendering SVG files"
HOMEPAGE = "http://ftp.gnome.org/pub/GNOME/sources/librsvg/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2+"
LIC_FILES_CHKSUM = "file://COPYING;md5=94d55d512a9ba36caa9b7df079bae19f \
                    file://rsvg.h;beginline=3;endline=24;md5=20b4113c4909bbf0d67e006778302bc6"

SECTION = "x11/utils"
DEPENDS = "cairo glib-2.0 gdk-pixbuf fontconfig freetype libxml2 pango"
DEPENDS += "gdk-pixbuf-native"
BBCLASSEXTEND = "native"

PR = "r12"

inherit autotools pkgconfig gnomebase gtk-doc pixbufcache

SRC_URI += "file://doc_Makefile.patch \
            file://librsvg-CVE-2011-3146.patch \
           "

SRC_URI[archive.md5sum] = "4b00d0fee130c936644892c152f42db7"
SRC_URI[archive.sha256sum] = "91b98051f352fab8a6257688d6b2fd665b4648ed66144861f2f853ccf876d334"

EXTRA_OECONF = "--without-svgz"

PACKAGECONFIG ??= "croco gdkpixbuf"

# Support embedded CSS stylesheets (recommended upstream)
PACKAGECONFIG[croco] = "--with-croco,--without-croco,libcroco"
# gdk-pixbuf loader
PACKAGECONFIG[gdkpixbuf] = "--enable-pixbuf-loader,--disable-pixbuf-loader"
# GTK+ 2 theme engine
PACKAGECONFIG[gtk] = "--enable-gtk-theme,--disable-gtk-theme,gtk+"

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

PIXBUF_PACKAGES = "librsvg-gtk"
PARALLEL_MAKE = ""

PIXBUFCACHE_SYSROOT_DEPS_append_class-native = " harfbuzz-native:do_populate_sysroot_setscene pango-native:do_populate_sysroot_setscene icu-native:do_populate_sysroot_setscene"
