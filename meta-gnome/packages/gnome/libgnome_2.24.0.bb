DESCRIPTION = "Gnome application programming libraries"
LICENSE = "GPL"
SECTION = "x11/gnome/libs"
PR = "r0"

inherit gnome lib_package

DEPENDS = "gconf-native gnome-vfs libbonobo esound"

EXTRA_OECONF = "--disable-gtk-doc"

FILES_${PN} += "${libdir}/bonobo/servers ${libdir}/bonobo/monikers/*.so \
                ${datadir}/gnome-background-properties ${datadir}/pixmaps"
FILES_${PN}-dev += "${libdir}/bonobo/monikers/*a"
FILES_${PN}-dbg += "${libdir}/bonobo/monikers/.debug"

do_stage() {
	autotools_stage_all
}
