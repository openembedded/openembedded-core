require table.inc

PV = "0.3.0+svn${SRCDATE}"
PR = "r0"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/trunk/toys;module=table;proto=http \
           file://fixes.patch;patch=1"

S = "${WORKDIR}/table"


