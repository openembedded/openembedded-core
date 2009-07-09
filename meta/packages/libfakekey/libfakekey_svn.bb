DESCRIPTION = "Matchbox keyboard"
LICENSE = "GPL"
DEPENDS = "libxtst"
SECTION = "x11/wm"
PV = "0.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools_stage pkgconfig gettext
