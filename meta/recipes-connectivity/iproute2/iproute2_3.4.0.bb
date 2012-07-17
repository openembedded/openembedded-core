require iproute2.inc

PR = "r0"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch"

SRC_URI[md5sum] = "879d3fac4e90809598b2864ec4a0cbf8"
SRC_URI[sha256sum] = "38e846e412b2fa235a447b50c20ad1e9770d1b3ed4d3ab18ca0b18c6e8b79ba4"
