require clutter-gst.inc

PV = "0.8.0+svnr${SRCREV}"
PR = "r0"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=${PN};proto=http"

S = "${WORKDIR}/${PN}"
