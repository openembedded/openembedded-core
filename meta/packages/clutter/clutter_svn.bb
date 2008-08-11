require clutter.inc

PV = "0.8.0+svnr${SRCREV}"
PR = "r0"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=clutter;proto=http \
           file://enable_tests.patch;patch=1;maxrev=2989 \
           file://enable-tests-r2990.patch;patch=1;minrev=2990"

S = "${WORKDIR}/clutter"


