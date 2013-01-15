require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
  file://ofono \
  file://missing-ssize_t.patch \
  file://obsolete_automake_macros.patch \
"

PR = "r2"

SRC_URI[md5sum] = "aa8924c0e8de3ec7ac5e41fe7df4cb99"
SRC_URI[sha256sum] = "733b75bfd1b2a1925b6de532989c496b8ae17a746691120ef64cceb00b3ef751"

EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"
CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
