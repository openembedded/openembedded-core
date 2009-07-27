require clutter.inc
require clutter-package.inc

PV = "0.9.0+git${SRCPV}"
PR = "r4"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=master \
           file://enable_tests.patch;patch=1 "
S = "${WORKDIR}/git"

BASE_CONF += "--disable-introspection"

