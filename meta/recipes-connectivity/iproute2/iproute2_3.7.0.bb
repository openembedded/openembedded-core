require iproute2.inc

PR = "r0"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch"

SRC_URI[md5sum] = "b07241b267036de9a79ca5b69acf8593"
SRC_URI[sha256sum] = "6b0e76d7adb8b9b65f70571f75d72db7c2548eff7813cace9e267065c3c0cb34"

# CFLAGS are computed in Makefile and reference CCOPTS
#
EXTRA_OEMAKE_append = " CCOPTS='${CFLAGS}'"
