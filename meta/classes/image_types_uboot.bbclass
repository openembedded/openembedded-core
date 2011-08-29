inherit image_types kernel-arch

oe_mkimage () {
    mkimage -A ${UBOOT_ARCH} -O linux -T ramdisk -C gzip -n ${IMAGE_NAME} \
        -d ${DEPLOY_DIR_IMAGE}/$1 ${DEPLOY_DIR_IMAGE}/$1.u-boot
}

IMAGE_DEPENDS_ext2.gz.u-boot = "genext2fs-native e2fsprogs-native u-boot-mkimage-native"
IMAGE_CMD_ext2.gz.u-boot      = "${IMAGE_CMD_ext2.gz} \
                                 oe_mkimage ${IMAGE_NAME}.rootfs.ext2.gz"

IMAGE_DEPENDS_ext3.gz.u-boot = "genext2fs-native e2fsprogs-native u-boot-mkimage-native"
IMAGE_CMD_ext3.gz.u-boot      = "${IMAGE_CMD_ext3.gz} \
                                 oe_mkimage ${IMAGE_NAME}.rootfs.ext3.gz"

IMAGE_DEPENDS_ext4.gz.u-boot = "genext2fs-native e2fsprogs-native u-boot-mkimage-native"
IMAGE_CMD_ext4.gz.u-boot      = "${IMAGE_CMD_ext4.gz} \
                                 oe_mkimage ${IMAGE_NAME}.rootfs.ext4.gz"

IMAGE_TYPES += "ext2.gz.u-boot ext3.gz.u-boot ext4.gz.u-boot"
