require pseudo.inc

SRCREV = "363a94bb851046f62648d7c96c749e899bd0648e"
PV = "1.4.4+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo;protocol=git"

S = "${WORKDIR}/git"

