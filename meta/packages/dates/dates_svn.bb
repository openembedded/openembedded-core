LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ libglade eds-dbus"
DESCRIPTION = "Dates is a calendar application."

PV = "0.1+svn${SRCDATE}"
S = "${WORKDIR}/trunk"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"

inherit autotools pkgconfig gtk-icon-cache

FILES_${PN} += "${datadir}/pixmaps/dates.png"

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${D}/${datadir}/icons/hicolor/48x48/apps/dates.png ${D}/${datadir}/pixmaps/
}
