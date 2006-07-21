DESCRIPTION = "Matchbox gtk theme configuration application."
LICENSE = "GPL"
DEPENDS = "gconf libglade gtk+"
RDEPENDS = "settings-daemon"
PV = "0.0+svn${SRCDATE}"
S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

inherit autotools pkgconfig

