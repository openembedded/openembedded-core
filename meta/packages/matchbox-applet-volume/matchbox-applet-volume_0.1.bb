DESCRIPTION = "Matchbox Volume Applet"
LICENSE = "GPL"
DEPENDS = "matchbox-wm libmatchbox"
SECTION = "x11/wm"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/mb-applet-volume/${PV}/mb-applet-volume-${PV}.tar.gz"
S = "${WORKDIR}/mb-applet-volume-${PV}"

inherit autotools pkgconfig

FILES_${PN} = "${bindir} ${datadir}/applications ${datadir}/pixmaps"
 
