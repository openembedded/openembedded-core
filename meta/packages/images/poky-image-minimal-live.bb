DESCRIPTION = "Minimal Bootable Live Image"

require poky-image-live.inc

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-minimal-${MACHINE}.ext3"

do_bootimg[depends] += "poky-image-minimal:do_rootfs"
