DESCRIPTION = "Minimal Bootable Live Image"

require moblin-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/moblin-image-minimal-${MACHINE}.ext3"

do_bootimg[depends] += "moblin-image-minimal:do_rootfs"
