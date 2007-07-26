require gtk+.inc

PR = "r12"

SRC_URI = "ftp://ftp.gtk.org/pub/gtk/v2.6/gtk+-${PV}.tar.bz2 \
           file://no-demos.patch;patch=1 \
           file://no-xwc.patch;patch=1 \
           file://automake-lossage.patch;patch=1 \
           file://gtk+-handhelds.patch;patch=1 \
	   file://hardcoded_libtool.patch;patch=1 \
	   file://disable-tooltips.patch;patch=1 \
	   file://gtklabel-resize-patch;patch=1 \
	   file://menu-deactivate.patch;patch=1 \
	   file://scroll-timings.patch;patch=1 \
	   file://filesystem-volumes.patch;patch=1 \
	   file://filechooser-respect-style.patch;patch=1 \
	   file://filechooser-default.patch;patch=1 \
	   file://toggle-font.diff;patch=1;pnum=0 \
	   file://combo-arrow-size.patch;patch=1;pnum=0 \
        	   file://range-no-redraw.patch;patch=1;pnum=0 \
	   "

EXTRA_OECONF = "--without-libtiff --disable-xkb --disable-glibtest"

LIBV = "2.4.0"
LEAD_SONAME = "libgtk-x11*"


do_configure_prepend() {
        for i in `find . -name "Makefile.am"`   
        do
                sed -i -e s,-DG_DISABLE_DEPRECATED,-DSED_ROCKS_DUDES, $i
        done
}


PACKAGES_DYNAMIC = "gdk-pixbuf-loader-* gtk-immodule-*"

python populate_packages_prepend () {
	import os.path

	prologue = bb.data.getVar("postinst_prologue", d, 1)

	gtk_libdir = bb.data.expand('${libdir}/gtk-2.0/${LIBV}', d)
	loaders_root = os.path.join(gtk_libdir, 'loaders')
	immodules_root = os.path.join(gtk_libdir, 'immodules')

	do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', prologue + 'gdk-pixbuf-query-loaders > /etc/gtk-2.0/gdk-pixbuf.loaders')
	do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk-immodule-%s', 'GTK input module for %s', prologue + 'gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules')

        if (bb.data.getVar('DEBIAN_NAMES', d, 1)):
                bb.data.setVar('PKG_${PN}', 'libgtk-2.0', d)
}
