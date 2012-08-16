require iproute2.inc

PR = "r0"

SRC_URI = "${KERNELORG_MIRROR}/linux/utils/net/${BPN}/${BPN}-${PV}.tar.xz \
           file://configure-cross.patch"

SRC_URI[md5sum] = "ab1fc3512c852ba0a929f6e589804e78"
SRC_URI[sha256sum] = "ea885642b1d9f8c843a325b85926a75bd36fb4801b37c7707cbd397fb120aa3d"
