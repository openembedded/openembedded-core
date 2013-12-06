require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
          "

SRC_URI[md5sum] = "f87386aaaecafab95607fd10e8152c68"
SRC_URI[sha256sum] = "44f600475d27a421688cda2294efec38513473a740c24ead78eb20005f08f111"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
