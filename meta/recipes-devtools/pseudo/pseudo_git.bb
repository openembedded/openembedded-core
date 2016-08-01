require pseudo.inc

SRCREV = "f4b1c752186f4d08f1fadb0ea10ebcde9b0ea251"
PV = "1.8.1+git${SRCPV}"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           file://moreretries.patch"

S = "${WORKDIR}/git"

