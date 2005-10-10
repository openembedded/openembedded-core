DESCRIPTION = "Settings-daemon is a bridge between xst/gpe-confd and gconf"
LICENSE = "GPL"
DEPENDS = "gconf xst glib-2.0"
SECTION = "gpe"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
	   file://70settings-daemon"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext

FILES_${PN} = 	"${bindir} ${sysconfdir}"

do_install_append () {
	install -d ${D}/${sysconfdir}/X11/Xsession.d
	install -m 755 ${WORKDIR}/70settings-daemon ${D}/${sysconfdir}/X11/Xsession.d/
}
