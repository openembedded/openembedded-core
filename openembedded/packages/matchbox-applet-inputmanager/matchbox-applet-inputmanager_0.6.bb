DESCRIPTION = "Matchbox input manager"
LICENSE = "GPL"
DEPENDS = "matchbox-wm libmatchbox"
SECTION = "x11/wm"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/mb-applet-input-manager/${PV}/mb-applet-input-manager-${PV}.tar.gz"
S = "${WORKDIR}/mb-applet-input-manager-${PV}"

inherit autotools pkgconfig

FILES_${PN} = "${bindir} ${datadir}/applications ${datadir}/pixmaps"
 
