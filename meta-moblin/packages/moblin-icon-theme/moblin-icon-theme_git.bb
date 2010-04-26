
SRC_URI = "git://git.moblin.org/${PN}.git;protocol=git"
PV = "0.0+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

BROKEN = "1"

S = "${WORKDIR}/git"

inherit autotools
