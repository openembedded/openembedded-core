#
# Copyright (C) 2007 OpenedHand Ltd.
#
PR = "r3"

IMAGE_INSTALL = "task-poky-boot"

IMAGE_LINGUAS = " "

require poky-image.inc

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "rm -rf ${IMAGE_ROOTFS}/usr/lib/ipkg/"
