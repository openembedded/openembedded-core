DESCRIPTION = "Matchbox Terminal"
LICENSE = "GPL"
DEPENDS = "gtk+ vte"
SECTION = "x11/utils"
PV = "0.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/matchbox/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools pkgconfig
