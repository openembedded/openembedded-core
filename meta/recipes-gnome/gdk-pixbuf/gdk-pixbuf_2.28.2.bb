DESCRIPTION = "Image loading library for GTK+"
HOMEPAGE = "http://www.gtk.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gdk-pixbuf/gdk-pixbuf.h;endline=26;md5=5066b71daefeff678494fffa3040aba9"

SECTION = "libs"

DEPENDS = "libpng glib-2.0 jpeg"
DEPENDS_append_linuxstdbase = " virtual/libx11"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz \
           file://hardcoded_libtool.patch \
           file://configure_fix.patch \
           file://extending-libinstall-dependencies.patch \
           "

SRC_URI[md5sum] = "04c04e5042acd85cff6c439aba1995f1"
SRC_URI[sha256sum] = "183113c2eb2232963e88864a6a54fd963dbfeb1a3679fb0d3456f9e0b79e4617"

inherit autotools pkgconfig gettext pixbufcache

LIBV = "2.10.0"

EXTRA_OECONF = "\
  --without-libtiff \
  --with-libpng \
  ${X11DEPENDS} \
  --disable-introspection \
"
X11DEPENDS = "--without-x11"
X11DEPENDS_linuxstdbase = "${@base_contains('DISTRO_FEATURES', 'x11', '--with-x11', '--without-x11', d)}"
X11DEPENDS_class-native = "--without-x11"

PACKAGES =+ "${PN}-xlib"

FILES_${PN}-xlib = "${libdir}/*pixbuf_xlib*${SOLIBS}"
ALLOW_EMPTY_${PN}-xlib = "1"

FILES_${PN} = "${bindir}/gdk-pixbuf-query-loaders \
	${bindir}/gdk-pixbuf-pixdata \
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

PACKAGES_DYNAMIC += "^gdk-pixbuf-loader-.*"
PACKAGES_DYNAMIC_class-native = ""

python populate_packages_prepend () {
    postinst_pixbufloader = d.getVar("postinst_pixbufloader", True)

    loaders_root = d.expand('${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders')

    d.setVar('PIXBUF_PACKAGES', ' '.join(do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s')))
}

do_install_append_class-native() {
	find ${D}${libdir} -name "libpixbufloader-*.la" -exec rm \{\} \;

	create_wrapper ${D}/${bindir}/gdk-pixbuf-csource \
		GDK_PIXBUF_MODULE_FILE=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/${LIBV}/loaders.cache

	create_wrapper ${D}/${bindir}/gdk-pixbuf-pixdata \
		GDK_PIXBUF_MODULE_FILE=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/${LIBV}/loaders.cache

	create_wrapper ${D}/${bindir}/gdk-pixbuf-query-loaders \
		GDK_PIXBUF_MODULE_FILE=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/${LIBV}/loaders.cache \
		GDK_PIXBUF_MODULEDIR=${STAGING_LIBDIR_NATIVE}/gdk-pixbuf-2.0/${LIBV}/loaders
}
BBCLASSEXTEND = "native"
