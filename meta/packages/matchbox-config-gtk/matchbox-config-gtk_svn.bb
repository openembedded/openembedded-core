DESCRIPTION = "Matchbox GTK+ theme configuration application."
LICENSE = "GPL"
DEPENDS = "gconf gtk+"
RDEPENDS = "settings-daemon"

PV = "0.0+svn${SRCDATE}"
PR = "r2"

S = "${WORKDIR}/${PN}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http \
        file://no-handed.patch;patch=1;pnum=0"

inherit autotools pkgconfig

