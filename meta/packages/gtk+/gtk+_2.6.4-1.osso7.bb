LICENSE = "LGPL"
DESCRIPTION = "GTK+ is a multi-platform toolkit for creating graphical user interfaces. Offering a complete \
set of widgets, GTK+ is suitable for projects ranging from small one-off projects to complete application suites."
HOMEPAGE = "http://www.gtk.org"
SECTION = "libs"
PRIORITY = "optional"
MAINTAINER = "Koen Kooi <koen@handhelds.org>"
DEPENDS = "glib-2.0 pango atk jpeg libpng libxext libxcursor gtk-doc libgcrypt"
PR = "r1"

S = "${WORKDIR}/gtk+2.0-2.6.4/upstream/tarballs/gtk+-2.6.4/"

SRC_URI = "http://stage.maemo.org:80/pool/maemo/ossw/source/g/gtk+2.0/gtk+2.0_${PV}.tar.gz \
           file://no-demos.patch;patch=1 \
           file://no-xwc.patch;patch=1 \
           file://automake-lossage.patch;patch=1 \
	   file://spinbutton.patch;patch=1 \
	   file://hardcoded_libtool.patch;patch=1 \
	   file://disable-tooltips.patch;patch=1 \
	   file://gtklabel-resize-patch;patch=1 \
	   file://xsettings.patch;patch=1 \
	   file://scroll-timings.patch;patch=1 \
	   file://small-gtkfilesel.patch;patch=1 \
	   file://migration.patch;patch=1;pnum=0 \
	file://000_gtk+-2.0.6-exportsymbols.patch;patch=1 \
	file://000_gtk+-2.2.0-buildfix-immodule.patch;patch=1 \
	file://002_xpmico.patch;patch=1 \
	file://003_iconcache.patch;patch=1 \
	file://configure.diff;patch=1 \
	file://configure.in.diff;patch=1 \
	file://gdkwindow-x11.c.diff;patch=1 \
	file://gtk.h.diff;patch=1 \
	file://gtk_Makefile.am.diff;patch=1 \
	file://gtk_Makefile.in.diff;patch=1 \
	file://gtkalias.h.diff;patch=1 \
	file://gtkbutton.c.diff;patch=1 \
	file://gtkbutton.h.diff;patch=1 \
	file://gtkcalendar.c.diff;patch=1 \
	file://gtkcalendar.h.diff;patch=1 \
	file://gtkcellrenderertext.c.diff;patch=1 \
	file://gtkcellrenderertoggle.c.diff;patch=1 \
	file://gtkcellview.c.diff;patch=1 \
	file://gtkcellview.h.diff;patch=1 \
	file://gtkcheckbutton.c.diff;patch=1 \
	file://gtkcombobox.c.diff;patch=1 \
	file://gtkcomboboxentry.c.diff;patch=1 \
	file://gtkcontainer.c.diff;patch=1 \
	file://gtkdialog.c.diff;patch=1 \
	file://gtkdnd.c.diff;patch=1 \
	file://gtkentry.c.diff;patch=1 \
	file://gtkenums.h.diff;patch=1 \
	file://gtkfilesystem.c.diff;patch=1 \
	file://gtkfilesystem.h.diff;patch=1 \
	file://gtkframe.c.diff;patch=1 \
	file://gtkhashtable.c.diff;patch=1 \
	file://gtkhashtable.h.diff;patch=1 \
	file://gtkhbbox.c.diff;patch=1 \
	file://gtkhseparator.c.diff;patch=1 \
	file://gtkiconfactory.c.diff;patch=1 \
	file://gtkicontheme.c.diff;patch=1 \
	file://gtkimcontext.c.diff;patch=1 \
	file://gtkimcontext.h.diff;patch=1 \
	file://gtkimmulticontext.c.diff;patch=1 \
	file://gtklabel.c.diff;patch=1 \
	file://gtklabel.h.diff;patch=1 \
	file://gtkmain.c.diff;patch=1 \
	file://gtkmarshal.c.diff;patch=1 \
	file://gtkmarshal.h.diff;patch=1 \
	file://gtkmarshalers.c.diff;patch=1 \
	file://gtkmarshalers.h.diff;patch=1 \
	file://gtkmenu.c.diff;patch=1 \
	file://gtkmenu.h.diff;patch=1 \
	file://gtkmenuitem.c.diff;patch=1 \
	file://gtkmenuitem.h.diff;patch=1 \
	file://gtkmenushell.c.diff;patch=1 \
	file://gtkmenushell.h.diff;patch=1 \
	file://gtknotebook.c.diff;patch=1 \
	file://gtkprogress.c.diff;patch=1 \
	file://gtkprogressbar.c.diff;patch=1 \
	file://gtkradiobutton.c.diff;patch=1 \
	file://gtkrange.c.diff;patch=1 \
	file://gtkrange.h.diff;patch=1 \
	file://gtkrbtree.c.diff;patch=1 \
	file://gtkrc.c.diff;patch=1 \
	file://gtkrc.h.diff;patch=1 \
	file://gtkscrolledwindow.c.diff;patch=1 \
	file://gtkseparator.c.diff;patch=1 \
	file://gtkseparatortoolitem.c.diff;patch=1 \
	file://gtksettings.c.diff;patch=1 \
	file://gtkspinbutton.c.diff;patch=1 \
	file://gtkstyle.c.diff;patch=1 \
	file://gtkstyle.h.diff;patch=1 \
	file://gtktable.c.diff;patch=1 \
	file://gtktextbuffer.c.diff;patch=1 \
	file://gtktextbuffer.h.diff;patch=1 \
	file://gtktextbufferserialize.c.diff;patch=1 \
	file://gtktextbufferserialize.h.diff;patch=1 \
	file://gtktextview.c.diff;patch=1 \
	file://gtktoolbar.c.diff;patch=1 \
	file://gtktoolbutton.c.diff;patch=1 \
	file://gtktoolbutton.h.diff;patch=1 \
	file://gtktreemodelfilter.c.diff;patch=1 \
	file://gtktreemodelsort.c.diff;patch=1 \
	file://gtktreeprivate.h.diff;patch=1 \
	file://gtktreeselection.c.diff;patch=1 \
	file://gtktreeview.c.diff;patch=1 \
	file://gtktreeview.h.diff;patch=1 \
	file://gtktreeviewcolumn.c.diff;patch=1 \
	file://gtktypebuiltins.c.diff;patch=1 \
	file://gtktypebuiltins.h.diff;patch=1 \
	file://gtkvseparator.c.diff;patch=1 \
	file://gtkwidget.c.diff;patch=1 \
	file://gtkwidget.h.diff;patch=1 \
	file://gtkwindow.c.diff;patch=1 \
	file://gtkwindow.h.diff;patch=1 \
	file://io-gif-animation.c.diff;patch=1 \
	file://io-gif.c.diff;patch=1" 

inherit autotools pkgconfig

FILES_${PN} = "${bindir}/gdk-pixbuf-query-loaders \
	${bindir}/gtk-query-immodules-2.0 \
	${libdir}/lib*.so.* \
	${datadir}/themes ${sysconfdir} \
	${libdir}/gtk-2.0/${LIBV}/engines/libpixmap.so"
FILES_${PN}-dev += "${datadir}/gtk-2.0/include ${libdir}/gtk-2.0/include ${bindir}/gdk-pixbuf-csource"

RRECOMMENDS_${PN} = "glibc-gconv-iso8859-1"

EXTRA_OECONF = "--without-libtiff --disable-xkb --disable-glibtest --enable-display-migration"
# --disable-cruft

LIBV = "2.4.0"

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

PACKAGES_DYNAMIC = "gdk-pixbuf-loader-* gtk-immodule-*"

python populate_packages_prepend () {
	import os.path

	gtk_libdir = bb.data.expand('${libdir}/gtk-2.0/${LIBV}', d)
	loaders_root = os.path.join(gtk_libdir, 'loaders')
	immodules_root = os.path.join(gtk_libdir, 'immodules')

	do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', 'gdk-pixbuf-query-loaders > /etc/gtk-2.0/gdk-pixbuf.loaders')
	do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk-immodule-%s', 'GTK input module for %s', 'gtk-query-immodules > /etc/gtk-2.0/gtk.immodules')

        if (bb.data.getVar('DEBIAN_NAMES', d, 1)):
                bb.data.setVar('PKG_${PN}', 'libgtk-2.0', d)
}
