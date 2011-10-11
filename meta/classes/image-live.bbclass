
AUTO_SYSLINUXCFG = "1"
INITRD_IMAGE ?= "core-image-minimal-initramfs"
INITRD ?= "${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}.cpio.gz"
APPEND += "root=/dev/ram0 "
TIMEOUT = "10"
LABELS += "boot install"

ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_BASENAME}-${MACHINE}.ext3"

do_bootimg[depends] += "${INITRD_IMAGE}:do_rootfs"
do_bootimg[depends] += "${IMAGE_BASENAME}:do_rootfs"

inherit bootimg
