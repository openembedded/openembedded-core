require clutter-gst.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.5.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=${PN};proto=http \
           file://autofoo.patch;patch=1"

S = "${WORKDIR}/${PN}"
