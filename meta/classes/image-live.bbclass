
AUTO_SYSLINUXCFG = "1"
INITRD_IMAGE ?= "core-image-minimal-initramfs"
INITRD ?= "${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}.cpio.gz"
SYSLINUX_ROOT ?= "root=/dev/ram0"
SYSLINUX_TIMEOUT ?= "50"
SYSLINUX_LABELS ?= "boot install"
LABELS_append = " ${SYSLINUX_LABELS} "

ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4"

do_bootimg[depends] += "${INITRD_IMAGE}:do_image_complete"
do_bootimg[depends] += "${PN}:do_image_ext4"

inherit bootimg

IMAGE_TYPEDEP_live = "ext4"
IMAGE_TYPEDEP_iso = "ext4"
IMAGE_TYPEDEP_hddimg = "ext4"
IMAGE_TYPES_MASKED += "live hddimg iso"
