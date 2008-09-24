SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you."
LICENSE = "GPL"
DEPENDS = "startup-notification gtk+ gconf clutter gdk-pixbuf-csource-native"
PR = "r0"
PV = "2.25.1+gitr${SRCREV}"
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

FILES_${PN} += "${datadir}/themes"

do_stage () {
	 autotools_stage_all
}

