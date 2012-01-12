SUMMARY = "Multi-platform toolkit for creating GUIs"
DESCRIPTION = "GTK+ is a multi-platform toolkit for creating graphical user interfaces. Offering a complete \
set of widgets, GTK+ is suitable for projects ranging from small one-off projects to complete application suites."
HOMEPAGE = "http://www.gtk.org"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2+ & LGPLv2.1+"

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

SECTION = "libs"

X11DEPENDS = "virtual/libx11 libxext libxcursor libxrandr libxdamage libxrender libxcomposite"
DEPENDS = "glib-2.0 pango atk jpeg libpng gtk-doc-native gdk-pixbuf-native docbook-utils-native \
 libgcrypt cairo gdk-pixbuf"

PACKAGECONFIG ??= "${@base_contains('DISTRO_FEATURES', 'x11', 'x11', '', d)} \
 ${@base_contains('DISTRO_FEATURES', 'gtk-directfb', 'gtk-directfb', '', d)}"

# for gtk+ 2.x, have to pick either x11 or directfb not both
# for gtk+ 3.x, PACKAGECONFIG[gtk-directfb] should be removed because directfb will not be support as
# a backend.
PACKAGECONFIG[x11] = "--with-x=yes --with-gdktarget=x11,--with-x=no,${X11DEPENDS}"
PACKAGECONFIG[gtk-directfb] = "--with-gdktarget=directfb,,directfb"

inherit autotools pkgconfig

PACKAGES += "libgail gtk-demo"

FILES_${PN} = "${bindir}/gtk-update-icon-cache \
	${bindir}/gtk-query-immodules-2.0 \
	${libdir}/lib*${SOLIBS} \
	${datadir}/themes ${sysconfdir} \
	${libdir}/gtk-2.0/${LIBV}/engines/libpixmap.so"

FILES_${PN}-dev += " \
        ${datadir}/gtk-2.0/include \
	${libdir}/gtk-2.0/include \
	${libdir}/gtk-2.0/modules/*.la \
	${libdir}/gtk-2.0/${LIBV}/loaders/*.la \
	${libdir}/gtk-2.0/${LIBV}/immodules/*.la \
	${libdir}/gtk-2.0/${LIBV}/printbackends/*.la \
	${libdir}/gtk-2.0/${LIBV}/engines/*.la \
	${bindir}/gtk-builder-convert"

FILES_${PN}-dbg += " \
	${libdir}/gtk-2.0/modules/.debug/* \
        ${libdir}/gtk-2.0/${LIBV}/loaders/.debug/* \
	${libdir}/gtk-2.0/${LIBV}/immodules/.debug/* \
	${libdir}/gtk-2.0/${LIBV}/engines/.debug/* \
	${libdir}/gtk-2.0/${LIBV}/printbackends/.debug/*"

FILES_gtk-demo = " \
 ${datadir}/gtk-2.0/demo/* \
 ${bindir}/gtk-demo \
 "

FILES_libgail = " \
	${libdir}/gtk-2.0/modules/libgail.so \
	${libdir}/gtk-2.0/modules/libferret.so \
	"

GTKBASE_RRECOMMENDS ?= "liberation-fonts gdk-pixbuf-loader-png gdk-pixbuf-loader-jpeg gdk-pixbuf-loader-gif gdk-pixbuf-loader-xpm shared-mime-info"
GTKGLIBC_RRECOMMENDS ?= "${GTKBASE_RRECOMMENDS} glibc-gconv-iso8859-1"

RRECOMMENDS_${PN} = "${GTKBASE_RRECOMMENDS}"
RRECOMMENDS_${PN}_libc-glibc = "${GTKGLIBC_RRECOMMENDS}"

do_install () {
	autotools_do_install

	install -d ${D}${sysconfdir}/gtk-2.0

	mkdir -p ${D}${libdir}/gtk-2.0/include
	install -m 0644 gdk/gdkconfig.h ${D}${libdir}/gtk-2.0/include/gdkconfig.h

	install -m 0644 gtk/gtkfilechooserprivate.h ${D}${includedir}/gtk-2.0/gtk/
	install -m 0644 gtk/gtkfilechooserutils.h   ${D}${includedir}/gtk-2.0/gtk/
	install -m 0644 gtk/gtkfilesystemmodel.h    ${D}${includedir}/gtk-2.0/gtk/
}

SYSROOT_PREPROCESS_FUNCS += "gtk_sysroot_preprocess"

gtk_sysroot_preprocess () {
	if [ -e ${D}${bindir}/gtk-builder-convert ]; then
		install -d ${SYSROOT_DESTDIR}${bindir_crossscripts}/
		install -m 755 ${D}${bindir}/gtk-builder-convert ${SYSROOT_DESTDIR}${bindir_crossscripts}/
	fi
}

postinst_prologue() {
if [ "x$D" != "x" ]; then
  exit 1
fi

}
