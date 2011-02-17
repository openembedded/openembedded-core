SUMMARY = "Multi-platform toolkit for creating GUIs"
DESCRIPTION = "GTK+ is a multi-platform toolkit for creating graphical user interfaces. Offering a complete \
set of widgets, GTK+ is suitable for projects ranging from small one-off projects to complete application suites."
HOMEPAGE = "http://www.gtk.org"
BUGTRACKER = "https://bugzilla.gnome.org/"

LICENSE = "LGPLv2 & LGPLv2+ & LGPLv2.1+"

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7"

SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "glib-2.0 pango atk jpeg libpng libxext libxcursor \
           gtk-doc-native docbook-utils-native libxrandr libgcrypt \
           libxdamage libxrender libxcomposite cairo gdk-pixbuf"

inherit autotools pkgconfig

FILES_${PN} = "${bindir}/gtk-update-icon-cache \
	${bindir}/gtk-query-immodules-2.0 \
	${libdir}/lib*${SOLIBS} \
	${datadir}/themes ${sysconfdir} \
	${libdir}/gtk-2.0/${LIBV}/engines/libpixmap.so"

FILES_${PN}-dev += " \
        ${datadir}/gtk-2.0/include \
	${libdir}/gtk-2.0/include \
	${libdir}/gtk-2.0/${LIBV}/loaders/*.la \
	${libdir}/gtk-2.0/${LIBV}/immodules/*.la \
	${libdir}/gtk-2.0/${LIBV}/engines/*.la \
	${bindir}/gtk-builder-convert"

FILES_${PN}-dbg += " \
        ${libdir}/gtk-2.0/${LIBV}/loaders/.debug/* \
	${libdir}/gtk-2.0/${LIBV}/immodules/.debug/* \
	${libdir}/gtk-2.0/${LIBV}/engines/.debug/* \
	${libdir}/gtk-2.0/${LIBV}/printbackends/.debug/*"

BASE_RRECOMMENDS = "ttf-dejavu-sans"
BASE_RRECOMMENDS_angstrom = "ttf-dejavu-sans gdk-pixbuf-loader-png gdk-pixbuf-loader-jpeg gdk-pixbuf-loader-gif gdk-pixbuf-loader-xpm"
BASE_RRECOMMENDS_poky = "gdk-pixbuf-loader-png gdk-pixbuf-loader-jpeg gdk-pixbuf-loader-gif gdk-pixbuf-loader-xpm shared-mime-info"

GLIBC_RRECOMMENDS= "${BASE_RRECOMMENDS} glibc-gconv-iso8859-1"

RRECOMMENDS_${PN} = "${BASE_RRECOMMENDS}"
RRECOMMENDS_${PN}_linux = "${GLIBC_RRECOMMENDS}"
RRECOMMENDS_${PN}_linux-gnueabi = "${GLIBC_RRECOMMENDS}"

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
