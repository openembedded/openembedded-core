require clutter.inc

PV = "0.8.0+git${SRCREV}"
PR = "r2"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=master \
           file://enable_tests.patch;patch=1;maxrev=2989 \
           file://enable-tests-r2990.patch;patch=1;minrev=2990"

S = "${WORKDIR}/git"


