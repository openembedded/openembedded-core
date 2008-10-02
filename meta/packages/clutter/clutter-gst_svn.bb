require clutter-gst.inc

PV = "0.8.0+svnr${SRCREV}"
PR = "r1"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=${PN};proto=http \
           file://autofoo.patch;patch=1"

S = "${WORKDIR}/${PN}"
