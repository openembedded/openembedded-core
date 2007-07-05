DESCRIPTION = "Matchbox Window Manager Desktop"
LICENSE = "GPL"
DEPENDS = "gtk+ startup-notification"
SECTION = "x11/wm"
PV = "2.0+svn${SRCDATE}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN}-2;proto=http"

EXTRA_OECONF = "--enable-startup-notification"

S = "${WORKDIR}/${PN}-2"

inherit autotools pkgconfig
