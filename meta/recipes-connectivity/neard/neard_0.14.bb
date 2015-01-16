require neard.inc

SRC_URI = "${KERNELORG_MIRROR}/linux/network/nfc/${BPN}-${PV}.tar.xz \
           file://parallel-build.patch \
           file://neard.in \
           file://neard.service.in \
           file://Makefile.am-fix-parallel-issue.patch \
          "
SRC_URI[md5sum] = "692ba2653d60155255244c87396c486b"
SRC_URI[sha256sum] = "6ea724b443d39d679168fc7776a965d1f64727c3735391df2c01469ee7cd8cca"

