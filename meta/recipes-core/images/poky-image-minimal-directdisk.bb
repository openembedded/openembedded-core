require poky-image-directdisk.inc

DESCRIPTION = "Minimal Bootable Direct Disk Image"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-minimal-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "poky-image-minimal:do_rootfs"


