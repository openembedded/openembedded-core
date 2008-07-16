require clutter-box2d.inc

PV = "0.0+svnr${SRCREV}"
PR = "r3"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=${PN};proto=http \
           file://clutter08.patch;patch=1 "

S = "${WORKDIR}/${PN}"


