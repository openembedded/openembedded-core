require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "
SRC_URI[md5sum] = "b741a02c6dda5818d18011d572874493"
SRC_URI[sha256sum] = "09e406636e7598e46d5d4f7b928bf5db57049d65dbeb9a496005957ee16f6000"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
