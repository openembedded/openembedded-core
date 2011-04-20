DESCRIPTION = "Minimal Bootable Live Image"

require core-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "core-image-minimal:do_rootfs"
