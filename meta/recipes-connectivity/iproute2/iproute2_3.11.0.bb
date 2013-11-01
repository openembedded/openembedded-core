require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "

SRC_URI[md5sum] = "d7ffb27bc9f0d80577b1f3fb9d1a7689"
SRC_URI[sha256sum] = "0ab31b52b5fd5ff0d3cf03f2068f05eeb4f0b37d107070fbbaacac94df5e88b7"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
