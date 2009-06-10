require clutter.inc

PV = "0.9.0+git${SRCPV}"
PR = "r3"

SRC_URI = "git://git.clutter-project.org/clutter.git;protocol=git;branch=master \
           file://enable_tests.patch;patch=1 "
S = "${WORKDIR}/git"

BASE_CONF += "--disable-introspection"

