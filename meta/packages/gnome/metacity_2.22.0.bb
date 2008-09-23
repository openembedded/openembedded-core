SECTION = "x11/wm"
DESCRIPTION = "Metacity is the boring window manager for the adult in you."
LICENSE = "GPL"
DEPENDS = "startup-notification gtk+ gconf"
PR = "r2"

inherit gnome update-alternatives

SRC_URI += "file://no-csource.patch;patch=1"

ALTERNATIVE_NAME = "x-window-manager"
ALTERNATIVE_LINK = "${bindir}/x-window-manager"
ALTERNATIVE_PATH = "${bindir}/metacity"
ALTERNATIVE_PRIORITY = "10"

EXTRA_OECONF += "--disable-verbose \
	         --disable-xinerama"

FILES_${PN} += "${datadir}/themes"

do_stage () {
	 autotools_stage_all
}

