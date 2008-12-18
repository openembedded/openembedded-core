require clutter.inc

PV = "0.8.0+git${SRCREV}"
PR = "r3"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=master \
           file://enable_tests.patch;patch=1 "
S = "${WORKDIR}/git"


