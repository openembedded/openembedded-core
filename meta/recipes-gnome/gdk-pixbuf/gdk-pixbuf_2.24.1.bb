DESCRIPTION = "Image loading library for GTK+"
HOMEPAGE = "http://www.gtk.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gdk-pixbuf/gdk-pixbuf.h;endline=26;md5=5066b71daefeff678494fffa3040aba9"

SECTION = "libs"

DEPENDS = "libpng glib-2.0 jpeg"
DEPENDS_append_linuxstdbase = " virtual/libx11"

SRC_URI = "http://ftp.acc.umu.se/pub/GNOME/sources/gdk-pixbuf/2.24/gdk-pixbuf-${PV}.tar.xz \
           file://configure_nm.patch \
           file://hardcoded_libtool.patch \
           file://configure_fix.patch \
           file://extending-libinstall-dependencies.patch \
           "

SRC_URI[md5sum] = "72f39b34b20f68148c1609bd27415412"
SRC_URI[sha256sum] = "da7a3f00db360913716368e19e336402755cafa93769f3cfa28a969303e4bee1"

PR = "r7"

inherit autotools pkgconfig gettext

LIBV = "2.10.0"

EXTRA_OECONF = "\
  --without-libtiff \
  --with-libpng \
  ${X11DEPENDS} \
  --disable-introspection \
"
X11DEPENDS = "--without-x11"
X11DEPENDS_linuxstdbase = "${@base_contains('DISTRO_FEATURES', 'x11', '--with-x11', '--without-x11', d)}"
X11DEPENDS_virtclass-native = "--without-x11"

PACKAGES =+ "${PN}-xlib"

FILES_${PN}-xlib = "${libdir}/*pixbuf_xlib*${SOLIBS}"
ALLOW_EMPTY_${PN}-xlib = "1"

FILES_${PN} = "${bindir}/gdk-pixbuf-query-loaders \
	${libdir}/lib*.so.*"

FILES_${PN}-dev += " \
	${bindir}/gdk-pixbuf-csource \
	${includedir}/* \
	${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders/*.la \
"

FILES_${PN}-dbg += " \
        ${libdir}/.debug/* \
	${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders/.debug/* \
"

postinst_pixbufloader () {
if [ "x$D" != "x" ]; then
    exit 1
fi

GDK_PIXBUF_MODULEDIR=${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders gdk-pixbuf-query-loaders --update-cache

if [ -x ${bindir}/gtk-update-icon-cache ] && [ -d ${datadir}/icons ]; then
    for icondir in /usr/share/icons/*; do
        if [ -d ${icondir} ]; then
            gtk-update-icon-cache -t -q ${icondir}
        fi
    done
fi
}

PACKAGES_DYNAMIC += "gdk-pixbuf-loader-*"
PACKAGES_DYNAMIC_virtclass-native = ""

python populate_packages_prepend () {
    postinst_pixbufloader = d.getVar("postinst_pixbufloader", True)

    loaders_root = d.expand('${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders')

    do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', postinst_pixbufloader)
}

do_install_append_virtclass-native() {
#Use wrapper script rather than binary as required libtool library is not installed now
	GDK_PIXBUF_MODULEDIR=${D}${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders ${S}/gdk-pixbuf/gdk-pixbuf-query-loaders > ${D}${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders.cache
	sed -i -e 's#${D}##g' ${D}${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders.cache
	find ${D}${libdir} -name "libpixbufloader-*.la" -exec rm \{\} \;

	create_wrapper ${D}/${bindir}/gdk-pixbuf-csource \
		GDK_PIXBUF_MODULE_FILE=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/${LIBV}/loaders.cache

	create_wrapper ${D}/${bindir}/gdk-pixbuf-query-loaders \
		GDK_PIXBUF_MODULE_FILE=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/${LIBV}/loaders.cache
}
BBCLASSEXTEND = "native"
