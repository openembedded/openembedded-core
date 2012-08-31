require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.bz2 \
  file://ofono \
"
PR = "r1"

SRC_URI[md5sum] = "dab284efb8cc4143d6f53d0fcc37b696"
SRC_URI[sha256sum] = "e7931ac0dbe165eb0e16a38b7cb4acca59b704f356ec13583d58027135e3efa5"


EXTRA_OECONF += "\
    --enable-test \
    ${@base_contains('DISTRO_FEATURES', 'bluetooth','--enable-bluetooth', '--disable-bluetooth', d)} \
"
CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
