require pseudo.inc

SRCREV = "f0375c9aaefbccfd41aebbf6d332bb4d9e8f980c"
PV = "1.3+git${SRCPV}"
PR = "r25"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git"

S = "${WORKDIR}/git"

