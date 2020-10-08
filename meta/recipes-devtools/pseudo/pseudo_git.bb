require pseudo.inc

SRC_URI = "git://git.yoctoproject.org/pseudo;branch=oe-core \
           file://0001-configure-Prune-PIE-flags.patch \
           file://fallback-passwd \
           file://fallback-group \
           "

SRCREV = "07141f919dc8582460f984ed1d2fb73e05f82ef3"
S = "${WORKDIR}/git"
PV = "1.9.0+git${SRCPV}"

# error: use of undeclared identifier '_STAT_VER'
COMPATIBLE_HOST_libc-musl = 'null'
