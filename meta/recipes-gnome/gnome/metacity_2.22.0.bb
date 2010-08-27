SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you."
LICENSE = "GPL"
DEPENDS = "startup-notification gtk+ gconf gdk-pixbuf-csource-native"
PR = "r3"

inherit gnome update-alternatives

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/metacity"
ALTERNATIVE_PRIORITY = "10"

EXTRA_OECONF += "--disable-verbose \
	         --disable-xinerama"

FILES_${PN} += "${datadir}/themes"
