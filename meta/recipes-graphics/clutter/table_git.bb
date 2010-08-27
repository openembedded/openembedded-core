require table.inc

PV = "0.3.0+git${SRCPV}"
PR = "r1"

SRC_URI = "git://git.clutter-project.org/toys.git;protocol=git \
           file://fixes.patch;patch=1"

S = "${WORKDIR}/git/table"


