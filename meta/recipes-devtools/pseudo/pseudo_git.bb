require pseudo.inc

SRCREV = "e076225ccdd10dabe3d6097c0682d8c88bd58753"
PV = "1.5+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo;protocol=git"

S = "${WORKDIR}/git"

