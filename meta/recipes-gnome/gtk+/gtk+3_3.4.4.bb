SUMMARY = "Multi-platform toolkit for creating GUIs"
DESCRIPTION = "GTK+ is a multi-platform toolkit for creating graphical user interfaces. Offering a complete \
set of widgets, GTK+ is suitable for projects ranging from small one-off projects to complete application suites."
HOMEPAGE = "http://www.gtk.org"
BUGTRACKER = "https://bugzilla.gnome.org/"
SECTION = "libs"

DEPENDS = "glib-2.0 pango atk jpeg libpng libxext libxcursor \
           docbook-utils-native libxrandr libgcrypt \
           libxdamage libxrender libxcomposite libxi cairo gdk-pixbuf gdk-pixbuf-native"

LICENSE = "LGPLv2 & LGPLv2+ & LGPLv2.1+"

LIC_FILES_CHKSUM = "file://COPYING;md5=5f30f0716dfdd0d91eb439ebec522ec2 \
                    file://gtk/gtk.h;endline=25;md5=1d8dc0fccdbfa26287a271dce88af737 \
                    file://gdk/gdk.h;endline=25;md5=c920ce39dc88c6f06d3e7c50e08086f2 \
                    file://tests/testgtk.c;endline=25;md5=cb732daee1d82af7a2bf953cf3cf26f1"

SRC_URI = "http://download.gnome.org/sources/gtk+/3.4/gtk+-${PV}.tar.xz \
           file://cross.patch"
SRC_URI[md5sum] = "1b2cf29502a6394e8d4b30f7f5bb9131"
SRC_URI[sha256sum] = "f154e460075034da4c0ce89c320025dcd459da2a1fdf32d92a09522eaca242c7"

inherit autotools pkgconfig gtk-doc update-alternatives gtk-immodules-cache

S = "${WORKDIR}/gtk+-${PV}"

# This should be in autotools.bbclass, but until something elses uses it putting
# it here avoids rebuilding everything.
export PKG_CONFIG_FOR_BUILD = "${STAGING_BINDIR_NATIVE}/pkg-config-native"

do_configure_prepend() {
    # Do this because the configure script is running ./libtool directly
    ln -s ${TARGET_PREFIX}libtool libtool || true
}

# Forcibly disable the GTK+ 2 dependency as we don't want to natively build the
# entire GTK+ stack, or need GTK+ 2 for gtk-update-icon-cache.
EXTRA_OECONF += " \
                 --disable-gtk2-dependency \
                 --disable-glibtest \
                 --enable-modules \
                 --disable-cups \
"

do_install_append() {
	mv ${D}${bindir}/gtk-update-icon-cache ${D}${bindir}/gtk-update-icon-cache-3.0
}

PACKAGES =+ "${PN}-demo"
LIBV = "3.0.0"

FILES_${PN}-demo = "${bindir}/gtk3-demo \
                    ${bindir}/gtk3-demo-application \
                    ${bindir}/gtk3-widget-factory \
                    ${datadir}/gtk-3.0/demo"

FILES_${PN} = "${bindir}/gtk-update-icon-cache-3.0 \
               ${bindir}/gtk-query-immodules-3.0 \
               ${bindir}/gtk-launch \
               ${libdir}/lib*${SOLIBS} \
               ${datadir}/themes ${sysconfdir} ${datadir}/glib-2.0/schemas/ \
               ${libdir}/gtk-3.0/${LIBV}/engines/libpixmap.so \
               ${libdir}/gtk-3.0/modules/*.so"

FILES_${PN}-dev += " \
                    ${datadir}/gtk-3.0/gtkbuilder.rng \
                    ${datadir}/gtk-3.0/include \
                    ${libdir}/gtk-3.0/include \
                    ${libdir}/gtk-3.0/${LIBV}/loaders/*.la \
                    ${libdir}/gtk-3.0/${LIBV}/immodules/*.la \
                    ${libdir}/gtk-3.0/3.0.0/printbackends/*.la \
                    ${libdir}/gtk-3.0/${LIBV}/engines/*.la \
                    ${libdir}/gtk-3.0/modules/*.la \
                    ${bindir}/gtk-builder-convert"

FILES_${PN}-dbg += " \
                    ${libdir}/gtk-3.0/${LIBV}/loaders/.debug \
                    ${libdir}/gtk-3.0/${LIBV}/immodules/.debug \
                    ${libdir}/gtk-3.0/${LIBV}/engines/.debug \
                    ${libdir}/gtk-3.0/${LIBV}/printbackends/.debug \
                    ${libdir}/gtk-3.0/modules/.debug"


PACKAGES_DYNAMIC += "^gtk3-immodule-.* ^gtk3-printbackend-.*"

ALTERNATIVE_${PN} = "gtk-update-icon-cache"
ALTERNATIVE_TARGET[gtk-update-icon-cache] = "${bindir}/gtk-update-icon-cache-3.0"

python populate_packages_prepend () {
    import os.path

    gtk_libdir = d.expand('${libdir}/gtk-3.0/${LIBV}')
    immodules_root = os.path.join(gtk_libdir, 'immodules')
    printmodules_root = os.path.join(gtk_libdir, 'printbackends');

    d.setVar('GTKIMMODULES_PACKAGES', ' '.join(do_split_packages(d, immodules_root, '^im-(.*)\.so$', 'gtk3-immodule-%s', 'GTK input module for %s')))
    do_split_packages(d, printmodules_root, '^libprintbackend-(.*)\.so$', 'gtk3-printbackend-%s', 'GTK printbackend module for %s')

    if (d.getVar('DEBIAN_NAMES', 1)):
        d.setVar('PKG_${PN}', 'libgtk-3.0')
}

