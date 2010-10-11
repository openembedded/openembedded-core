require recipes-core/images/poky-image-directdisk.inc

DESCRIPTION = "SDK Direct Disk Image"

LICENSE = "MIT"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sdk-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "poky-image-sdk:do_rootfs"
