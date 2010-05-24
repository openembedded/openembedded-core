#
# Copyright (C) 2010 Intel Corporation.
#
require moblin-image-directdisk.inc

DESCRIPTION = "Moblin Direct Disk Image"

ROOTFS = "${DEPLOY_DIR_IMAGE}/moblin-image-netbook-${MACHINE}.ext3"

do_bootdirectdisk[depends] += "moblin-image-netbook:do_rootfs"
