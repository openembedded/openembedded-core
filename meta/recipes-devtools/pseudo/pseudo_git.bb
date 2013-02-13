require pseudo.inc

SRCREV = "011b401fb6be38d739215e455588af4dcd707e17"
PV = "1.4.5+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo;protocol=git"

S = "${WORKDIR}/git"

