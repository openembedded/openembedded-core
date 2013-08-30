require neard.inc

SRC_URI = "git://git.kernel.org/pub/scm/network/nfc/neard.git \
           file://neard.in \
           file://neard.service.in \
          "

S = "${WORKDIR}/git"
SRCREV = "eb486bf35e24d7d1db61350f5ab393a0c880523d"
PV = "0.10+git${SRCPV}"
PR = "r0"
