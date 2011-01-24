require recipes-core/images/poky-image-directdisk.inc

DESCRIPTION = "Sato SDK Direct Disk Image"

LICENSE = "MIT"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sato-sdk-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "poky-image-sato-sdk:do_rootfs"
