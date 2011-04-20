DESCRIPTION = "LSB Bootable Live Image"

require recipes-core/images/core-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-lsb-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "core-image-lsb:do_rootfs"
