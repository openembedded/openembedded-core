require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "
SRC_URI[md5sum] = "bd9d7567bbb987c88120669f5e1a1092"
SRC_URI[sha256sum] = "bda38951c49f89ffc2e2fe85579ce616337b5d3a2f0319921fd071c838356bd4"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
