LICENSE = "LGPL"
DESCRIPTION = "GTK+ is a multi-platform toolkit for creating graphical user interfaces. Offering a complete \
set of widgets, GTK+ is suitable for projects ranging from small one-off projects to complete application suites."
HOMEPAGE = "http://www.gtk.org"
SECTION = "libs"
PRIORITY = "optional"
DEPENDS = "glib-2.0 pango atk jpeg libpng libxext libxcursor gtk-doc libgcrypt cairo cups"
PR = "r2"

# disable per default - untested and not all patches included.
DEFAULT_PREFERENCE = "-1" 

SRC_URI = "ftp://ftp.gtk.org/pub/gtk/v2.10/gtk+-${PV}.tar.bz2 \
           file://no-xwc.patch;patch=1 \
           file://automake-lossage.patch;patch=1 \
	   file://disable-tooltips.patch;patch=1 \
	   file://gtklabel-resize-patch;patch=1 \
	   file://menu-deactivate.patch;patch=1 \
	   file://xsettings.patch;patch=1 \
	   file://scroll-timings.patch;patch=1 \
	   file://small-gtkfilesel.patch;patch=1 \
	   file://migration.patch;patch=1;pnum=0 \
           file://run-iconcache.patch;patch=1 \
           file://disable-print.patch;patch=1 \
           file://hardcoded_libtool.patch;patch=1 \
           file://no-demos.patch;patch=1 \
           file://pangoxft2.10.6.diff;patch=1"
#           file://gtk+-handhelds.patch;patch=1
#	   file://single-click.patch;patch=1
#	   file://spinbutton.patch;patch=1 \

inherit autotools pkgconfig

FILES_${PN} = "${bindir}/gdk-pixbuf-query-loaders \
	${bindir}/gtk-update-icon-cache \
	${bindir}/gtk-query-immodules-2.0 \
	${libdir}/lib*.so.* \
	${datadir}/themes ${sysconfdir} \
	${libdir}/gtk-2.0/${LIBV}/engines/libpixmap.so"
FILES_${PN}-dev += " \
        ${datadir}/gtk-2.0/include \
        ${libdir}/gtk-2.0/include \
        ${libdir}/gtk-2.0/${LIBV}/loaders/*.la \
        ${libdir}/gtk-2.0/${LIBV}/immodules/*.la \
        ${libdir}/gtk-2.0/${LIBV}/engines/*.la \
        ${bindir}/gdk-pixbuf-csource"
FILES_${PN}-dbg += " \
        ${libdir}/gtk-2.0/${LIBV}/loaders/.debug/* \
        ${libdir}/gtk-2.0/${LIBV}/immodules/.debug/* \
        ${libdir}/gtk-2.0/${LIBV}/engines/.debug/*"


RRECOMMENDS_${PN} = "glibc-gconv-iso8859-1 ttf-dejavu-sans"
RRECOMMENDS_${PN}_angstrom = "glibc-gconv-iso8859-1 ttf-dejavu-sans gdk-pixbuf-loader-png gdk-pixbuf-loader-jpeg gdk-pixbuf-loader-gif gdk-pixbuf-loader-xpm"
RRECOMMENDS_${PN}_openzaurus = "glibc-gconv-iso8859-1 ttf-dejavu-sans gdk-pixbuf-loader-png gdk-pixbuf-loader-jpeg gdk-pixbuf-loader-gif gdk-pixbuf-loader-xpm"

EXTRA_OECONF = "--without-libtiff --disable-xkb --disable-glibtest --enable-display-migration"

LIBV = "2.10.0"

do_stage () {
	oe_libinstall -so -C gtk libgtk-x11-2.0 ${STAGING_LIBDIR}
	oe_libinstall -so -C gdk libgdk-x11-2.0 ${STAGING_LIBDIR}
	oe_libinstall -so -C contrib/gdk-pixbuf-xlib libgdk_pixbuf_xlib-2.0 ${STAGING_LIBDIR}
	oe_libinstall -so -C gdk-pixbuf libgdk_pixbuf-2.0 ${STAGING_LIBDIR}

	autotools_stage_includes

	mkdir -p ${STAGING_LIBDIR}/gtk-2.0/include
	install -m 0644 gdk/gdkconfig.h ${STAGING_LIBDIR}/gtk-2.0/include/gdkconfig.h

	install -m 0644 m4macros/gtk-2.0.m4 ${STAGING_DATADIR}/aclocal/
}

do_install_append () {
	install -d ${D}${sysconfdir}/gtk-2.0
}

postinst_prologue() {
if [ "x$D" != "x" ]; then
  exit 1
fi

}

PACKAGES_DYNAMIC = "gdk-pixbuf-loader-* gtk-immodule-* gtk-printbackend-*"

python populate_packages_prepend () {
	import os.path

	prologue = bb.data.getVar("postinst_prologue", d, 1)

	gtk_libdir = bb.data.expand('${libdir}/gtk-2.0/${LIBV}', d)
	loaders_root = os.path.join(gtk_libdir, 'loaders')
	immodules_root = os.path.join(gtk_libdir, 'immodules')
	printmodules_root = os.path.join(gtk_libdir, 'printbackends');

	do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', prologue + 'gdk-pixbuf-query-loaders > /etc/gtk-2.0/gdk-pixbuf.loaders')
	do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk-immodule-%s', 'GTK input module for %s', prologue + 'gtk-query-immodules > /etc/gtk-2.0/gtk.immodules')
	do_split_packages(d, printmodules_root, '^libprintbackend-(.*)\.so$', 'gtk-printbackend-%s', 'GTK printbackend module for %s')

        if (bb.data.getVar('DEBIAN_NAMES', d, 1)):
                bb.data.setVar('PKG_${PN}', 'libgtk-2.0', d)
}
