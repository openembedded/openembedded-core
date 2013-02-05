require pseudo.inc

SRCREV = "a01d7884e5f3acba1460cf6b500d28390e7af9f8"
PV = "1.4.3+git${SRCPV}"
PR = "r0"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo;protocol=git"

S = "${WORKDIR}/git"

