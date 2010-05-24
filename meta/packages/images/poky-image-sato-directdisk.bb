require poky-image-directdisk.inc

DESCRIPTION = "Sato Direct Disk Image"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sato-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "poky-image-sato:do_rootfs"
