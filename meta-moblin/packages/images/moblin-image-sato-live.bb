DESCRIPTION = "Sato Bootable Live Image"

require moblin-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/moblin-image-sato-${MACHINE}.ext3"

do_bootimg[depends] += "moblin-image-sato:do_rootfs"
