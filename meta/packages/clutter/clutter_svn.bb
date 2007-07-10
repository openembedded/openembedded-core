require clutter.inc

PV = "0.3.0+svn${SRCDATE}"
PR = "r7"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=clutter;proto=http \
	   file://enable_tests.patch;patch=1 "

S = "${WORKDIR}/clutter"


