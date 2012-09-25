require iproute2.inc

PR = "r1"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch"

SRC_URI[md5sum] = "d4425b44edd5eacd6099e672e4baacbf"
SRC_URI[sha256sum] = "36f2674e5436289f3ccfb0a58707aca9dcfa295d06afc36d2117674508f5ef72"
