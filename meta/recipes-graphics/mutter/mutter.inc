SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you. Mutter is metacity + clutter."
LICENSE = "GPLv2"
DEPENDS = "startup-notification gtk+ gconf clutter-1.8 gdk-pixbuf-native intltool glib-2.0-native"
# gobject-introspection
inherit gnome update-alternatives

ALTERNATIVE_${PN} = "x-window-manager"
ALTERNATIVE_TARGET[x-window-manager] = "${bindir}/mutter"
ALTERNATIVE_PRIORITY = "11"

EXTRA_OECONF += "--disable-verbose	\
	         --disable-xinerama	\
	         --without-introspection \
	         --without-libcanberra \
		 --with-clutter"

#RDEPENDS_${PN} = "zenity"

FILES_${PN} += "${datadir}/themes ${libdir}/mutter/plugins/*.so ${datadir}/gnome/wm-properties/"
FILES_${PN}-dbg += "${libdir}/mutter/plugins/.debug/*"

CFLAGS_FOR_BUILD += "-I${STAGING_INCDIR_NATIVE}/glib-2.0 -I${STAGING_INCDIR_NATIVE}/glib-2.0/include"
LDFLAGS_FOR_BUILD += "-L${STAGING_LIBDIR_NATIVE} -lglib-2.0"

do_configure_prepend () {
        echo "EXTRA_DIST=" > ${S}/gnome-doc-utils.make
}		

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type list --list-type string --set /apps/mutter/general/clutter_plugins '[default]'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type bool --set /apps/mutter/general/compositing_manager true
}

