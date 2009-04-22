SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you. Mutter is metacity + clutter."
LICENSE = "GPL"
DEPENDS = "startup-notification gtk+ gconf clutter gdk-pixbuf-csource-native intltool glib-2.0-native"
PR = "r8"
PV = "2.25.1+git${SRCREV}"
inherit gnome update-alternatives

SRC_URI = "git://git.gnome.org/mutter.git;protocol=git;branch=master \
           file://nodocs.patch;patch=1 \
	   file://crosscompile.patch;patch=1 \
           file://fix_pkgconfig.patch;patch=1"
S = "${WORKDIR}/git"

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/metacity"
ALTERNATIVE_PRIORITY = "11"

EXTRA_OECONF += "--disable-verbose	\
	         --disable-xinerama	\
		 --with-clutter"

FILES_${PN} += "${datadir}/themes ${libdir}/metacity/plugins/clutter/*.so"
FILES_${PN}-dbg += "${libdir}/metacity/plugins/clutter/.debug/*"

export CC_FOR_BUILD = "${BUILD_CC}"
export CFLAGS_FOR_BUILD = "${BUILD_CFLAGS} -I${STAGING_INCDIR_NATIVE}/glib-2.0 -I${STAGING_INCDIR_NATIVE}/glib-2.0/include"
export LDFLAGS_FOR_BUILD = "${BUILD_LDFLAGS} -L${STAGING_LIBDIR_NATIVE} -lglib-2.0"

do_configure_prepend () {
        echo "EXTRA_DIST=" > ${S}/gnome-doc-utils.make
}		

do_stage () {
	 autotools_stage_all
}

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type list --list-type string --set /apps/metacity/general/clutter_plugins '[default]'

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type bool --set /apps/metacity/general/compositing_manager true
}

