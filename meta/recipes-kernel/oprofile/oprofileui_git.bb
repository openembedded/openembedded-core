require oprofileui.inc

SRCREV = "b3116a4f80ae64bd280e6434d66f33ed492d449a"
PV = "0.0+git${SRCPV}"
PR = "r0"

S = "${WORKDIR}/git"

# Oprofileui at http://labs.o-hand.com/oprofileui/ is not maintained now.
SRC_URI = "git://git.yoctoproject.org/oprofileui;protocol=git"
