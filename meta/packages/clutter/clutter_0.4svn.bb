require clutter.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.4.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/branches;module=clutter-0-4;proto=http \
	   file://enable_tests.patch;patch=1 "

S = "${WORKDIR}/clutter-0-4"
