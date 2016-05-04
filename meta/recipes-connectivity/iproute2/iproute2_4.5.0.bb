require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
           file://iproute2-4.3.0-musl.patch \
          "
SRC_URI[md5sum] = "b9ee1cbba7e20e04dfdd4b3055181955"
SRC_URI[sha256sum] = "3f15292f53e465cb5bd6652961343ca64eb6936309ae75be5d5a541435bc494a"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
