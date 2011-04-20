require core-image-directdisk.inc

DESCRIPTION = "Minimal Bootable Direct Disk Image"

ROOTFS = "${DEPLOY_DIR_IMAGE}/core-image-minimal-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootdirectdisk[depends] += "core-image-minimal:do_rootfs"


