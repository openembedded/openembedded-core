LICENSE = "LGPL"
SECTION = "x11"
DEPENDS = "glib-2.0 gtk+ intltool eds-dbus"
RDEPENDS = "libedata-cal"
DESCRIPTION = "Dates is a calendar application."

DEFAULT_PREFERENCE = "-1"

PV = "0.0+svn${SRCDATE}"
S = "${WORKDIR}/trunk"

SRC_URI = "svn://svn.o-hand.com/repos/${PN};module=trunk;proto=http"

inherit autotools pkgconfig gtk-icon-cache

FILES_${PN} += "${datadir}/pixmaps/dates.png"

do_install_append () {
	install -d ${D}/${datadir}/pixmaps
	install -m 0644 ${D}/${datadir}/icons/hicolor/48x48/apps/dates.png ${D}/${datadir}/pixmaps/
}
