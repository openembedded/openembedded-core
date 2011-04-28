require oprofileui.inc

SRCREV = "0c3c32fa754c1d0b70e65767ea7048914f776396"
PV = "0.0+git${SRCPV}"
PR = "r4"

S = "${WORKDIR}/git"

# Oprofileui at http://labs.o-hand.com/oprofileui/ is not maintained now.
SRC_URI = "git://git.yoctoproject.org/oprofileui;protocol=git \
           file://dso_linking_change_build_fix.patch "
