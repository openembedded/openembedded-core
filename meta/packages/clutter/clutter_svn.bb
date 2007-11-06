require clutter.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.5.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=clutter;proto=http \
	   file://enable_tests.patch;patch=1 "

S = "${WORKDIR}/clutter"


