DESCRIPTION = "LSB Bootable Live Image"

require recipes-core/images/poky-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-lsb-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "poky-image-lsb:do_rootfs"
