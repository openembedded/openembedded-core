DESCRIPTION = "LSB SDK Bootable Live Image"

require recipes-core/images/poky-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-lsb-sdk-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "poky-image-lsb-sdk:do_rootfs"
