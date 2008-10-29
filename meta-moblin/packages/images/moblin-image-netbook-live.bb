#
# Copyright (C) 2008 Intel Corporation.
#

DESCRIPTION = "Netbook Bootable Live Image"

require moblin-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/moblin-image-netbook-${MACHINE}.ext3"

do_bootimg[depends] += "moblin-image-netbook:do_rootfs"
