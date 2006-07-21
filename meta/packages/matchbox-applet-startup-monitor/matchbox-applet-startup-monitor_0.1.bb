DESCRIPTION = "Matchbox Startup monitor applet"
LICENSE = "GPL"
DEPENDS = "libmatchbox startup-notification"
SECTION = "x11/wm"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/mb-applet-startup-monitor/${PV}/mb-applet-startup-monitor-${PV}.tar.gz"
S = "${WORKDIR}/mb-applet-startup-monitor-${PV}"

inherit autotools pkgconfig

FILES_${PN} = "${bindir} ${datadir}/applications ${datadir}/pixmaps"
 
