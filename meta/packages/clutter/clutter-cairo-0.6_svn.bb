require clutter-cairo.inc

PV = "0.8.0+svnr${SRCREV}"

DEPENDS += "clutter-0.6"

SRC_URI = "svn://svn.o-hand.com/repos/clutter/branches;module=clutter-cairo-0-8;proto=http \
           file://enable_examples.patch;patch=1"

S = "${WORKDIR}/${PN}"


