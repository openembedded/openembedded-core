require clutter.inc

PV = "0.7.0+svnr${SRCREV}"
PR = "r4"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=clutter;proto=http \
           file://enable_tests.patch;patch=1 "

S = "${WORKDIR}/clutter"


