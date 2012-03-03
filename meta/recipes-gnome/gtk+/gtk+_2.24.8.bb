require gtk+.inc

LIC_FILES_CHKSUM = "file://COPYING;md5=3bf50002aefd002f49e7bb854063f7e7 \
                    file://gtk/gtk.h;endline=27;md5=c59e0b4490dd135a5726ebf851f9b17f \
                    file://gdk/gdk.h;endline=27;md5=07db285ec208fb3e0bf7d861b0614202 \
                    file://tests/testgtk.c;endline=27;md5=262db5db5f776f9863e56df31423e24c"
SRC_URI = "http://download.gnome.org/sources/gtk+/2.24/gtk+-${PV}.tar.bz2 \
           file://xsettings.patch \
           file://run-iconcache.patch \
           file://configure-nm.patch \
           file://hardcoded_libtool.patch \
           file://cellrenderer-cairo.patch;striplevel=0 \
           file://toggle-font.diff;striplevel=0 \
           file://0001-bgo-584832-Duplicate-the-exec-string-returned-by-gtk.patch \
           file://doc-fixes.patch \
	  "

# TO MERGE
#           file://entry-cairo.patch;striplevel=0
#           file://filesystem-volumes.patch
#           file://filechooser-props.patch
#           file://filechooser-default.patch
#           file://filechooser-sizefix.patch
# temporary
#           file://gtklabel-resize-patch
#           file://menu-deactivate.patch
#        file://combo-arrow-size.patch;striplevel=0
#            file://configurefix.patch

PR = "r4"

SRC_URI[md5sum] = "0413187f7e596aef00ccd1b54776ff03"
SRC_URI[sha256sum] = "ac2325a65312922a6722a7c02a389f3f4072d79e13131485cc7b7226e2537043"

EXTRA_OECONF = "--without-libtiff --without-libjasper --enable-xkb --disable-glibtest --disable-cups --disable-xinerama"

LIBV = "2.10.0"

PACKAGES_DYNAMIC += "gtk-immodule-* gtk-printbackend-*"

python populate_packages_prepend () {
	import os.path

	prologue = d.getVar("postinst_prologue", True)

	gtk_libdir = d.expand('${libdir}/gtk-2.0/${LIBV}')
	immodules_root = os.path.join(gtk_libdir, 'immodules')
	printmodules_root = os.path.join(gtk_libdir, 'printbackends');

	do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk-immodule-%s', 'GTK input module for %s', prologue + 'gtk-query-immodules-2.0 > /etc/gtk-2.0/gtk.immodules')
	do_split_packages(d, printmodules_root, '^libprintbackend-(.*)\.so$', 'gtk-printbackend-%s', 'GTK printbackend module for %s')

        if (d.getVar('DEBIAN_NAMES', True)):
                d.setVar('PKG_${PN}', '${MLPREFIX}libgtk-2.0')
}
