DESCRIPTION = "LSB SDK Bootable Live Image"

require recipes-core/images/core-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-lsb-sdk-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "core-image-lsb-sdk:do_rootfs"
