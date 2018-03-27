require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BP}.tar.xz \
           file://configure-cross.patch \
           file://0001-libc-compat.h-add-musl-workaround.patch \
           file://0001-ip-Remove-unneed-header.patch \
          "

SRC_URI[md5sum] = "0681bf4664b2649ad4e12551a3a7a1f9"
SRC_URI[sha256sum] = "48d4616a99d7b609b7b795c0ae8ec57099fb0271ed89253e8772c02327798355"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
