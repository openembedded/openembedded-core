DESCRIPTION = "Bootable Live Sato SDK Image"

require recipes-core/images/poky-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sato-sdk-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "poky-image-sato-sdk:do_rootfs"
