require gtk+.inc

PR = "r6"

SRC_URI = "http://download.gnome.org/sources/gtk+/2.16/gtk+-${PV}.tar.bz2 \
           file://xsettings.patch;patch=1 \
           file://run-iconcache.patch;patch=1 \
           file://hardcoded_libtool.patch;patch=1 \
           file://no-demos.patch;patch=1 \
           file://cellrenderer-cairo.patch;patch=1;pnum=0 \
           file://toggle-font.diff;patch=1;pnum=0 \
	   file://0001-bgo-584832-Duplicate-the-exec-string-returned-by-gtk.patch;patch=1 \
# TO MERGE
#           file://entry-cairo.patch;patch=1;pnum=0 \
#           file://filesystem-volumes.patch;patch=1 \
#           file://filechooser-props.patch;patch=1 \
#           file://filechooser-default.patch;patch=1 \
#           file://filechooser-sizefix.patch;patch=1 \
# temporary
#           file://gtklabel-resize-patch;patch=1 \
#           file://menu-deactivate.patch;patch=1 \
#        file://combo-arrow-size.patch;patch=1;pnum=0 \
            file://disable-gio-png-sniff-test.diff;patch=1 \
	  "

EXTRA_OECONF = "--without-libtiff --without-libjasper --disable-xkb --disable-glibtest --disable-cups"

LIBV = "2.10.0"

PACKAGES_DYNAMIC += "gdk-pixbuf-loader-* gtk-immodule-* gtk-printbackend-*"

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
