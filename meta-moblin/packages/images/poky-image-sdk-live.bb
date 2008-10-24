DESCRIPTION = "Bootable Live SDK Image"

require poky-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sdk-${MACHINE}.ext3"

do_bootimg[depends] += "poky-image-sdk:do_rootfs"
