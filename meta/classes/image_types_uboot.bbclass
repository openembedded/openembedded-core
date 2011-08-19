oe_mkimage () {
    mkimage -A ${UBOOT_ARCH} -O linux -T ramdisk -C gzip -n ${IMAGE_NAME} \
        -d ${DEPLOY_DIR_IMAGE}/$1 ${DEPLOY_DIR_IMAGE}/$1.u-boot
}

UBOOT_IMAGE_DEPENDS = "genext2fs-native e2fsprogs-native u-boot-mkimage-native"

IMAGE_DEPENDS_ext2.gz.u-boot  = "${UBOOT_IMAGE_DEPENDS}"
IMAGE_CMD_ext2.gz.u-boot      = "${IMAGE_CMD_ext2.gz}; oe_mkimage ${IMAGE_NAME}.rootfs.ext2.gz"
IMAGE_DEPENDS_ext2.bz2.u-boot = "${UBOOT_IMAGE_DEPENDS}"
IMAGE_CMD_ext2.bz2.u-boot     = "${IMAGE_CMD_ext2.bz2}; oe_mkimage ${IMAGE_NAME}.rootfs.ext2.bz2"

IMAGE_DEPENDS_ext3.gz.u-boot  = "${UBOOT_IMAGE_DEPENDS}"
IMAGE_CMD_ext3.gz.u-boot      = "${IMAGE_CMD_ext3.gz}; oe_mkimage ${IMAGE_NAME}.rootfs.ext3.gz"
IMAGE_DEPENDS_ext3.bz2.u-boot = "${UBOOT_IMAGE_DEPENDS}"
IMAGE_CMD_ext3.bz2.u-boot     = "${IMAGE_CMD_ext3.bz2}; oe_mkimage ${IMAGE_NAME}.rootfs.ext3.bz2"

IMAGE_DEPENDS_ext4.gz.u-boot  = "${UBOOT_IMAGE_DEPENDS}"
IMAGE_CMD_ext4.gz.u-boot      = "${IMAGE_CMD_ext4.gz}; oe_mkimage ${IMAGE_NAME}.rootfs.ext4.gz"
IMAGE_DEPENDS_ext4.bz2.u-boot = "${UBOOT_IMAGE_DEPENDS}"
IMAGE_CMD_ext4.bz2.u-boot     = "${IMAGE_CMD_ext4.bz2}; oe_mkimage ${IMAGE_NAME}.rootfs.ext4.bz2"
