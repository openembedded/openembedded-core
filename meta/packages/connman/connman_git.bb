require connman.inc

PV       = "0.47+git${SRCPV}"
PR       = "r16"
S        = "${WORKDIR}/git"

SRC_URI  = "git://git.kernel.org/pub/scm/network/connman/connman.git;protocol=git \
            file://dbusperms.patch \
            file://connman "

