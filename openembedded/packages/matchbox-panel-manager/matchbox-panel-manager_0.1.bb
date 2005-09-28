DESCRIPTION = "Matchbox Panel Manager"
LICENSE = "GPL"
DEPENDS = "gtk+"
SECTION = "x11/wm"

SRC_URI = "http://projects.o-hand.com/matchbox/sources/${PN}/${PV}/${PN}-${PV}.tar.bz2"
S = "${WORKDIR}/${PN}-${PV}"

inherit autotools pkgconfig

FILES_${PN} = "${bindir} ${datadir}/applications ${datadir}/pixmaps"

