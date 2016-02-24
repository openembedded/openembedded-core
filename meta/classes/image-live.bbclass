
INITRD_IMAGE_LIVE ?= "core-image-minimal-initramfs"
INITRD_LIVE ?= "${DEPLOY_DIR_IMAGE}/${INITRD_IMAGE_LIVE}-${MACHINE}.cpio.gz"
SYSLINUX_ROOT_LIVE ?= "root=/dev/ram0"
SYSLINUX_LABELS_LIVE ?= "boot install"
LABELS_LIVE ?= "${SYSLINUX_LABELS_LIVE}"
SYSLINUX_CFG_LIVE ?= "${S}/syslinux_live.cfg"

ROOTFS ?= "${DEPLOY_DIR_IMAGE}/${IMAGE_LINK_NAME}.ext4"

do_bootimg[depends] += "${PN}:do_image_ext4"

inherit bootimg

IMAGE_TYPEDEP_live = "ext4"
IMAGE_TYPEDEP_iso = "ext4"
IMAGE_TYPEDEP_hddimg = "ext4"
IMAGE_TYPES_MASKED += "live hddimg iso"

python() {
    image_b = d.getVar('IMAGE_BASENAME', True)
    initrd_i = d.getVar('INITRD_IMAGE_LIVE', True)
    if image_b == initrd_i:
        bb.error('INITRD_IMAGE_LIVE %s cannot use image live, hddimg or iso.' % initrd_i)
        bb.fatal('Check IMAGE_FSTYPES and INITRAMFS_FSTYPES settings.')
    else:
        d.appendVarFlag('do_bootimg', 'depends', ' %s:do_image_complete' % initrd_i)
}
