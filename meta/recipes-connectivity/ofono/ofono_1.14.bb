require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
  file://ofono \
"

SRC_URI[md5sum] = "8bc398d86642408cc71d039f59c61538"
SRC_URI[sha256sum] = "84d28d37cbc47129628a78bf3e17323af1636dceb2494511dd44caa829fb277f"

CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
