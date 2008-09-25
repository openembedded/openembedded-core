DESCRIPTION = "Netbook Bootable Live Image"

require poky-image-live.inc

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-netbook-${MACHINE}.ext3"
TIMEOUT = "3"

do_bootimg[depends] += "poky-image-netbook:do_rootfs"
