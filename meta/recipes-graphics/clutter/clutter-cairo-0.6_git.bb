require clutter-cairo.inc

PV = "0.6.0+git${SRCREV}"
PR = "r1"

DEPENDS += "clutter-0.6"

SRC_URI = "git://git.clutter-project.org/clutter-cairo.git;protocol=git;branch=clutter-cairo-0-6 \
           file://enable_examples-0.6.patch;patch=1"

S = "${WORKDIR}/git"


