DESCRIPTION = "Sato Bootable Live Image"

require poky-image-live.inc

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-sato-${MACHINE}.ext3"
TIMEOUT = "3"

do_bootimg[depends] += "poky-image-sato:do_rootfs"
