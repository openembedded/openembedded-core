require recipes-core/images/core-image-directdisk.inc

DESCRIPTION = "Sato Direct Disk Image"

LICENSE = "MIT"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-sato-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "core-image-sato:do_rootfs"
