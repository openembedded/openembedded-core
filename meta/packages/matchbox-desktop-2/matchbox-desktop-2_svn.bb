DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPL"
DEPENDS = "gtk+ startup-notification"
RDEPENDS = "matchbox-common"
SECTION = "x11/wm"
PV = "0.0+svn${SRCDATE}"
PR = "r1"

PROVIDES_${PN} = matchbox-desktop
RPROVIDES_${PN} = matchbox-desktop
RREPLACES_${PN} = matchbox-desktop
RCONFLICTS_${PN} = matchbox-desktop

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig
