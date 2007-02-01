DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPL"
DEPENDS = "gtk+ startup-notification"
SECTION = "x11/wm"
PV = "0.0+svn${SRCDATE}"
DEFAULT_PREFERENCE = "-1"

RPROVIDES = matchbox-desktop
RREPLACES = matchbox-desktop
RCONFLICTS = matchbox-desktop

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

EXTRA_OECONF = "--enable-startup-notification"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig
