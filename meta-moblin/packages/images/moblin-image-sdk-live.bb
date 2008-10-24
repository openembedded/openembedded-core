DESCRIPTION = "Bootable Live SDK Image"

require moblin-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/moblin-image-sdk-${MACHINE}.ext3"

do_bootimg[depends] += "moblin-image-sdk:do_rootfs"
