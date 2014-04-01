SUMMARY = "Image loading library for GTK+"
HOMEPAGE = "http://www.gtk.org/"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gdk-pixbuf/gdk-pixbuf.h;endline=26;md5=5066b71daefeff678494fffa3040aba9"

SECTION = "libs"

DEPENDS = "glib-2.0"
DEPENDS_append_linuxstdbase = " virtual/libx11"

MAJ_VER = "${@oe.utils.trim_version("${PV}", 2)}"

SRC_URI = "${GNOME_MIRROR}/${BPN}/${MAJ_VER}/${BPN}-${PV}.tar.xz \
           file://hardcoded_libtool.patch \
           file://extending-libinstall-dependencies.patch \
           file://run-ptest \
           file://tests-check.patch \
           file://fatal-loader.patch \
           "

SRC_URI[md5sum] = "81161cc895eb43afd9ae7354b87e2261"
SRC_URI[sha256sum] = "c229c53f59573eab9410b53690a4b9db770312c80a4d84ecd6295aa894574494"

inherit autotools pkgconfig gettext pixbufcache ptest

LIBV = "2.10.0"

GDK_PIXBUF_LOADERS ?= "png jpeg"

PACKAGECONFIG ??= "${GDK_PIXBUF_LOADERS}"
PACKAGECONFIG_linuxstdbase = "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} ${GDK_PIXBUF_LOADERS}"
PACKAGECONFIG_class-native = "${GDK_PIXBUF_LOADERS}"

PACKAGECONFIG[png] = "--with-libpng,--without-libpng,libpng"
PACKAGECONFIG[jpeg] = "--with-libjpeg,--without-libjpeg,jpeg"
PACKAGECONFIG[tiff] = "--with-libtiff,--without-libtiff,tiff"
PACKAGECONFIG[jpeg2000] = "--with-libjasper,--without-libjasper,jasper"

# Use GIO to sniff image format instead of trying all loaders
PACKAGECONFIG[gio-sniff] = "--enable-gio-sniffing,--disable-gio-sniffing,,shared-mime-info"
PACKAGECONFIG[x11] = "--with-x11,--without-x11,virtual/libx11"

EXTRA_OECONF = "\
  --disable-introspection \
  ${@base_contains('DISTRO_FEATURES', 'ptest', '--enable-installed-tests', '--disable-installed-tests', d)} \
"

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

FILES_${PN}-ptest += "${libdir}/gdk-pixbuf/installed-tests \
                      ${datadir}/installed-tests/gdk-pixbuf"

RDEPENDS_${PN}-ptest += "gnome-desktop-testing"

PACKAGES_DYNAMIC += "^gdk-pixbuf-loader-.*"
PACKAGES_DYNAMIC_class-native = ""

python populate_packages_prepend () {
    postinst_pixbufloader = d.getVar("postinst_pixbufloader", True)

    loaders_root = d.expand('${libdir}/gdk-pixbuf-2.0/${LIBV}/loaders')

    packages = ' '.join(do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s'))
    d.setVar('PIXBUF_PACKAGES', packages)

    # The test suite exercises all the loaders, so ensure they are all
    # dependencies of the ptest package.
    d.appendVar("RDEPENDS_gdk-pixbuf-ptest", " " + packages)
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
