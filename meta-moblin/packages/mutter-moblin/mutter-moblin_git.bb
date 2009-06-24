DESCRIPTION = "A Moblin specific plugin for the Mutter composite window manager"
SECTION = "x11/wm"
LICENSE = "GPLv3"
DEPENDS = "nbtk mutter gnome-menus mojito libjana anerley clutter-mozembed"
PV = "2.25.2+git${SRCPV}"
PR = "r4"

SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git \
           file://startup-notify.patch;patch=1 \
           file://background-tile.png"

FILES_${PN} += "\
	${libdir}/metacity/plugins/clutter/*.so* \
	${datadir}/mutter-moblin-netbook-plugin \
	${datadir}/dbus-1/services \
	${datadir}/moblin-panel-applications/theme"
FILES_${PN}-dbg += "${libdir}/metacity/plugins/clutter/.debug/*"

S = "${WORKDIR}/git"

ASNEEDED = ""

EXTRA_OECONF = "--enable-ahoghill --enable-netpanel --enable-people"

inherit autotools_stage

do_configure_prepend () {
	rm -f ${S}/build/autotools/gtk-doc.m4
	cp ${WORKDIR}/background-tile.png ${S}/data/theme/panel/
}

pkg_postinst_${PN} () {
#!/bin/sh -e
if [ "x$D" != "x" ]; then
    exit 1
fi

. ${sysconfdir}/init.d/functions

gconftool-2 --config-source=xml::$D${sysconfdir}/gconf/gconf.xml.defaults --direct --type list --list-type string --set /apps/metacity/general/clutter_plugins '[moblin-netbook]'
} 
