DESCRIPTION = "Settings-daemon provides a bridge between gconf and xsettings"
LICENSE = "GPL"
DEPENDS = "gconf glib-2.0 gtk+"
RDEPENDS = "xrdb"
SECTION = "x11"
PV = "0.0+svnr${SRCREV}"

PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
	   file://70settings-daemon"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext gconf

FILES_${PN} = 	"${bindir}/* ${sysconfdir}"

do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d
	install -m 755 ${WORKDIR}/70settings-daemon ${D}/${sysconfdir}/X11/Xsession.d/
}

