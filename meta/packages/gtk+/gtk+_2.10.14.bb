require gtk+.inc

DEPENDS += "cairo"
PR = "r19"

# disable per default - untested and not all patches included.
DEFAULT_PREFERENCE = "-1" 

SRC_URI = "ftp://ftp.gtk.org/pub/gtk/v2.10/gtk+-${PV}.tar.bz2 \
           file://gtklabel-resize-patch;patch=1 \
           file://menu-deactivate.patch;patch=1 \
           file://run-iconcache.patch;patch=1 \
           file://disable-print.patch;patch=1 \
           file://hardcoded_libtool.patch;patch=1 \
           file://no-demos.patch;patch=1 \
           file://cellrenderer-cairo.patch;patch=1;pnum=0 \
           file://entry-cairo.patch;patch=1;pnum=0 \
           file://toggle-font.diff;patch=1;pnum=0 \
           file://combo-arrow-size.patch;patch=1;pnum=0 \
           file://range-no-redraw.patch;patch=1;pnum=0 \
           file://scrolled-placement.patch;patch=1;pnum=0 \
           file://filesystem-volumes.patch;patch=1 \
           file://filechooser-utils.patch;patch=1 \
           file://filechooser.patch;patch=1 \
           file://filechooser-default.patch;patch=1 \
           file://filechooser-respect-style.patch;patch=1"
#file://xsettings.patch;patch=1 \
#file://pangoxft2.10.6.diff;patch=1"


EXTRA_OECONF = "--without-libtiff --disable-xkb --disable-glibtest --enable-display-migration"

LIBV = "2.10.0"

PACKAGES_DYNAMIC = "gdk-pixbuf-loader-* gtk-immodule-* gtk-printbackend-*"

python populate_packages_prepend () {
	import os.path

	prologue = bb.data.getVar("postinst_prologue", d, 1)
	postinst_pixbufloader = bb.data.getVar("postinst_pixbufloader", d, 1)

	gtk_libdir = bb.data.expand('${libdir}/gtk-2.0/${LIBV}', d)
	loaders_root = os.path.join(gtk_libdir, 'loaders')
	immodules_root = os.path.join(gtk_libdir, 'immodules')
	printmodules_root = os.path.join(gtk_libdir, 'printbackends');

	do_split_packages(d, loaders_root, '^libpixbufloader-(.*)\.so$', 'gdk-pixbuf-loader-%s', 'GDK pixbuf loader for %s', postinst_pixbufloader)
	do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk-immodule-%s', 'GTK input module for %s', prologue + 'gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules')
	do_split_packages(d, printmodules_root, '^libprintbackend-(.*)\.so$', 'gtk-printbackend-%s', 'GTK printbackend module for %s')

        if (bb.data.getVar('DEBIAN_NAMES', d, 1)):
                bb.data.setVar('PKG_${PN}', 'libgtk-2.0', d)
}
