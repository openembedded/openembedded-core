require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "
SRC_URI[md5sum] = "5b1711c9d16071959052e369a2682d77"
SRC_URI[sha256sum] = "5359ed1f31839d8542a057c0c4233131ab9c28d8c41fc9c8484579d9c0b99af4"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
