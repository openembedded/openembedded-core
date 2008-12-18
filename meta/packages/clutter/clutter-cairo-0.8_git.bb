require clutter-cairo.inc

PV = "0.8.0+git${SRCREV}"

DEPENDS += "clutter-0.8"

SRC_URI = "git://git.clutter-project.org/clutter-cairo.git;protocol=git;branch=clutter-cairo-0-8 \
           file://enable_examples.patch;patch=1"

S = "${WORKDIR}/git"


