require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-iproute2-de-bash-scripts.patch \
           file://iproute2-4.3.0-musl.patch \
          "
SRC_URI[md5sum] = "1a2bbb80cfc7ab3f3e987e18b3207c2f"
SRC_URI[sha256sum] = "f03b1188dd6c039512424de82ff7a8f3b446680bd4e908ff42a7b9b137422995"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
