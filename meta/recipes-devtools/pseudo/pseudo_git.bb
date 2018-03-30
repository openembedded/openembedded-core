require pseudo.inc

SRC_URI = "git://git.yoctoproject.org/pseudo \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           file://moreretries.patch \
           file://toomanyfiles.patch \
           "

SRCREV = "19f18124f16c4c85405b140a1fb8cb3b31d865bf"
S = "${WORKDIR}/git"
PV = "1.9.0+git${SRCPV}"

