require clutter.inc

DEFAULT_PREFERENCE = "-1"

PV = "0.7.0+svnr${SRCREV}"
PR = "r2"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk;module=clutter;proto=http \
           file://configure_fix.patch;patch=1 \
           file://enable_tests.patch;patch=1 "

S = "${WORKDIR}/clutter"


