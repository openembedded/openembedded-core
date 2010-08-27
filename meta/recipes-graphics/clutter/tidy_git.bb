require tidy.inc

PV = "0.1.0+git${SRCPV}"
PR = "r7"

SRC_URI = "git://git.clutter-project.org/tidy.git;protocol=git \
           file://tidy-enable-tests.patch;patch=1"

S = "${WORKDIR}/git"

