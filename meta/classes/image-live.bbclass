
INITRD_IMAGE ?= "core-image-minimal-initramfs"
INITRD ?= "${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE}-${MACHINE}.cpio.gz"
SYSLINUX_ROOT ?= "root=/dev/ram0"
SYSLINUX_TIMEOUT ?= "50"
SYSLINUX_LABELS ?= "boot install"
LABELS_append = " ${SYSLINUX_LABELS} "

ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4"

do_bootimg[depends] += "${PN}:do_image_ext4"

inherit bootimg

IMAGE_TYPEDEP_live = "ext4"
IMAGE_TYPEDEP_iso = "ext4"
IMAGE_TYPEDEP_hddimg = "ext4"
IMAGE_TYPES_MASKED += "live hddimg iso"

python() {
    image_b = d.getVar('IMAGE_BASENAME', True)
    initrd_i = d.getVar('INITRD_IMAGE', True)
    if image_b == initrd_i:
        bb.error('INITRD_IMAGE %s cannot use image live, hddimg or iso.' % initrd_i)
        bb.fatal('Check IMAGE_FSTYPES and INITRAMFS_FSTYPES settings.')
    else:
        d.appendVarFlag('do_bootimg', 'depends', ' %s:do_image_complete' % initrd_i)
}
