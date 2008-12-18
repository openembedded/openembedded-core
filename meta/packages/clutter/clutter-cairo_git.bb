require clutter-cairo.inc

PV = "0.8.0+git${SRCREV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/clutter-cairo.git;protocol=git \
           file://enable_examples.patch;patch=1"

S = "${WORKDIR}/git"


