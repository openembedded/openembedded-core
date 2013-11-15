require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
  file://ofono \
"

SRC_URI[md5sum] = "78112668d8444bc28d4b3359efd866f6"
SRC_URI[sha256sum] = "3329c0af1e672777122981dfb9728b2c517f30f9d1b997e08e6177eb0109f0d3"

CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
