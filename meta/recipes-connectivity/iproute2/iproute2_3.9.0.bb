require iproute2.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch \
          "

SRC_URI[md5sum] = "fd9db28e4f411a1e74de65c919ae590f"
SRC_URI[sha256sum] = "16f027af432a05085813a2f859b7d42dafd29b8c035ead830d37565b7397592d"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
