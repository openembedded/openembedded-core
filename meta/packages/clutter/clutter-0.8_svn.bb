require clutter.inc

PE = "1"
PR = "r0"
PV = "0.8.0+svnr${SRCREV}"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/branches;module=clutter-0-8;proto=http \
	   file://enable_tests-0.8.patch;patch=1 "

S = "${WORKDIR}/clutter-0-8"
