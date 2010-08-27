DESCRIPTION = "libfakekey"
HOMEPAGE = "http://matchbox-project.org/"
BUGTRACKER = "http://bugzilla.openedhand.com/"

LICENSE = "LGPLv2+"

DEPENDS = "libxtst"
SECTION = "x11/wm"
PV = "0.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig gettext
