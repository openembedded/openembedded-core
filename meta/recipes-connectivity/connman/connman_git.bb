require connman.inc

SRCREV = "36e877dece72577a3ae6197eafd505d0e6e0c1d2"
PV       = "0.47+git${SRCPV}"
PR       = "r17"
S        = "${WORKDIR}/git"

SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git;protocol=git \
            file://dbusperms.patch \
            file://connman "

