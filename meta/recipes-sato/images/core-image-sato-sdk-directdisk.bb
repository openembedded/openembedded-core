require recipes-core/images/core-image-directdisk.inc

DESCRIPTION = "Sato SDK Direct Disk Image"

LICENSE = "MIT"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-sato-sdk-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "core-image-sato-sdk:do_rootfs"
