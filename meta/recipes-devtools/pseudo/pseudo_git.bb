require pseudo.inc

SRCREV = "398a264490713c912b4ce465251a8a82a7905f45"
PV = "1.4.1+git${SRCPV}"
PR = "r28"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo;protocol=git"

S = "${WORKDIR}/git"

