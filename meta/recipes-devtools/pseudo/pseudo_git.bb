require pseudo.inc

SRC_URI = "git://git.yoctoproject.org/pseudo;branch=oe-core \
           file://0001-configure-Prune-PIE-flags.patch \
           file://delete_mismatches.patch \
           file://add_ignore_paths.patch \
           file://abort_on_mismatch.patch \
           file://track_link_fds.patch \
           file://fallback-passwd \
           file://fallback-group \
           "

SRCREV = "d6b1b13c268d7246f0288d32d6b5eccc658cff4e"
S = "${WORKDIR}/git"
PV = "1.9.0+git${SRCPV}"

# error: use of undeclared identifier '_STAT_VER'
COMPATIBLE_HOST_libc-musl = 'null'
