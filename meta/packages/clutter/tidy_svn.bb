require tidy.inc

PV = "0.1.0+svnr${SRCREV}"
PR = "r7"

SRC_URI = "svn://svn.o-hand.com/repos/tidy;module=trunk;proto=http \
           file://tidy-enable-tests.patch;patch=1"

S = "${WORKDIR}/trunk"

