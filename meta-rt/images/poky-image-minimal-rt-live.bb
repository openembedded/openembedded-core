#
# Copyright (C) 2010 Intel Corporation.
#

DESCRIPTION = "Bootable Live Minimal Real-Time Linux Image"

require recipes-core/images/poky-image-live.inc

LABELS += "boot install"

ROOTFS = "${DEPLOY_DIR_IMAGE}/poky-image-minimal-rt-${MACHINE}.ext3"

LICENSE = "MIT"

do_bootimg[depends] += "poky-image-minimal-rt:do_rootfs"
