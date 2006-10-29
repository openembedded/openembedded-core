LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
DESCRIPTION = "Dates is a calendar application."

SRC_URI = "http://projects.o-hand.com/sources/dates/dates-0.1.tar.gz"

inherit autotools pkgconfig gtk-icon-cache

# EXTRA_OECONF = "--disable-debug"

FILES_${PN} += "${datadir}/pixmaps/dates.png"

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${D}/${datadir}/icons/hicolor/48x48/apps/dates.png ${D}/${datadir}/pixmaps/
}

