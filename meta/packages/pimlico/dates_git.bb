require dates.inc

PV = "0.4.9+git${SRCPV}"
S = "${WORKDIR}/git"

SRC_URI = "git://git.gnome.org/${PN};protocol=git"

SRC_URI_append_poky += "file://dates-owl-window-menu.patch;patch=1"
