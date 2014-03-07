require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "

SRC_URI[md5sum] = "45fb5427fc723a0001c72b92c931ba02"
SRC_URI[sha256sum] = "c4023d8d722a1ed673474ca0e5a2d61ebd747cc7001a91321757422d9074c97e"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
