require ofono.inc

SRC_URI  = "\
  ${KERNELORG_MIRROR}/linux/network/${BPN}/${BP}.tar.xz \
  file://ofono \
  file://Revert-test-Convert-to-Python-3.patch \
"

SRC_URI[md5sum] = "4d03de85239d8100dc7721bf0dad2bd2"
SRC_URI[sha256sum] = "978807a05e8904eb4e57d6533ed71e75676a55fa3819a39fe2c878f45dbf7af6"

CFLAGS_append_libc-uclibc = " -D_GNU_SOURCE"
