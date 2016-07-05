require pseudo.inc

SRCREV = "eb47d855a831b6dc0ad34890e84b8f6f483693df"
PV = "1.8.1+git${SRCPV}"

DEFAULT_PREFERENCE = "-1"

SRC_URI = "git://git.yoctoproject.org/pseudo \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           file://moreretries.patch"

S = "${WORKDIR}/git"

