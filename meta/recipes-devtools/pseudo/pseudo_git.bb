require pseudo.inc

SRCREV = "9b792e4cb91bc22374f47d0b0902dbc4ecd7d6dd"
PV = "1.4+git${SRCPV}"
PR = "r27"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo;protocol=git"

S = "${WORKDIR}/git"

