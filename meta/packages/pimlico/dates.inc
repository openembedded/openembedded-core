DESCRIPTION = "Dates is a calendar application."
LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus libowl"
RDEPENDS = "libedata-cal"

inherit autotools pkgconfig gtk-icon-cache

# EXTRA_OECONF = "--disable-debug"
EXTRA_OECONF = "--enable-owl=true --disable-dnd"

FILES_${PN} += "${datadir}/pixmaps/dates.png"

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${D}/${datadir}/icons/hicolor/48x48/apps/dates.png ${D}/${datadir}/pixmaps/
}

