#
# Copyright (C) 2010 Intel Corporation.
#

require recipes-core/images/poky-image-directdisk.inc

DESCRIPTION = "Bootable Minimal Real-Time Direct Disk Image"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-minimal-rt-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootdirectdisk[depends] += "poky-image-minimal-rt:do_rootfs"


