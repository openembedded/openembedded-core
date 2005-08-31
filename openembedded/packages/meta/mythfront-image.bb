export IMAGE_BASENAME = "mythfront-image"

IMAGE_ROOTFS_SIZE_ext2 = "85000"
IMAGE_ROOTFS_SIZE_ext2.gz = "85000"
IMAGE_LINGUAS = ""

MYTHFRONT_PACKAGES = "task-bootstrap task-mythfront"

export IPKG_INSTALL = "${MYTHFRONT_PACKAGES}"
DEPENDS = "${MYTHFRONT_PACKAGES}"

inherit image_ipk
LICENSE = MIT
