require pseudo.inc

SRC_URI = "git://git.yoctoproject.org/pseudo \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           file://moreretries.patch \
           file://toomanyfiles.patch \
           "

SRCREV = "d7c31a25e4b02af0c64e6be0b4b0a9ac4ffc9da2"
S = "${WORKDIR}/git"
PV = "1.9.0+git${SRCPV}"

