SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you."
LICENSE = "GPL"
DEPENDS = "startup-notification gtk+ gconf clutter gdk-pixbuf-csource-native"
PR = "r2"
PV = "2.25.1+git${SRCREV}"
inherit gnome update-alternatives

SRC_URI = "git://git.o-hand.com/metacity-clutter.git;protocol=git;branch=clutter"
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

do_stage () {
	 autotools_stage_all
}

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type list --list-type string --set /apps/metacity/general/clutter_plugins '[simple]'
}

