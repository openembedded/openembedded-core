DESCRIPTION = "Bootable Live Sato SDK Image"

require recipes-core/images/core-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-sato-sdk-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "core-image-sato-sdk:do_rootfs"
