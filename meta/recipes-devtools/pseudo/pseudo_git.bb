require pseudo.inc

SRC_URI = "git://git.yoctoproject.org/pseudo;branch=oe-core \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           "

SRCREV = "8317c0ab172db47dabcef909bae02cd77b1f1010"
S = "${WORKDIR}/git"
PV = "1.9.0+git${SRCPV}"

# error: use of undeclared identifier '_STAT_VER'
COMPATIBLE_HOST_libc-musl = 'null'
