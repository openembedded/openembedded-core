require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
  file://ofono \
  file://use-python3.patch \
"
SRC_URI[md5sum] = "27ea2f1a155231af49d520486b8ebc05"
SRC_URI[sha256sum] = "4dacdc61571aad7ce8b4412ed51d03bec3d4060b93ee68f8c69b26b223bdc975"
