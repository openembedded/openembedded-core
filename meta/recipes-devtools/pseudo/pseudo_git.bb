require pseudo.inc

SRCREV = "2e0189ba5368b1e88d509d0ab82bccb15cfb3653"
PV = "1.3.1+git${SRCPV}"
PR = "r26"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://github.com/wrpseudo/pseudo.git;protocol=git"

S = "${WORKDIR}/git"

