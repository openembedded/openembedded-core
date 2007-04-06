DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPL"
DEPENDS = "gtk+ startup-notification"
RDEPENDS = "matchbox-common"
SECTION = "x11/wm"
PV = "0.0+svn${SRCDATE}"

PROVIDES = matchbox-desktop
RPROVIDES = matchbox-desktop
RREPLACES = matchbox-desktop
RCONFLICTS = matchbox-desktop

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig
