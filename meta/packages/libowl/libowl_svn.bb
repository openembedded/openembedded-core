DESCRIPTION = "OpenedHand Widget Library"
HOMEPAGE = "http://www.o-hand.com"
LICENSE = "LGPL"
SECTION = "libs"
DEPENDS = "gtk+"
PV = "0.0+svnr${SRCREV}"
PR = "r5"

SRC_URI = "svn://svn.o-hand.com/repos/misc/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"

inherit autotools_stage pkgconfig
