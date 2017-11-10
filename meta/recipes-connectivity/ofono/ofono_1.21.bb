require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
  file://ofono \
"
SRC_URI[md5sum] = "bc2b818f6fe5725d0dd8591aff6640d7"
SRC_URI[sha256sum] = "a6b021cda0b444b772897cd637d5f455857fb5819b62c279a8302b44f9c7f2c3"
