#
# Copyright (C) 2007 OpenedHand Ltd.
#
PR = "r1"

IMAGE_INSTALL = "task-oh-boot"

IMAGE_LINGUAS = " "

require poky-image.inc

# remove not needed stuff (locale support and ipkg support)
ROOTFS_POSTPROCESS_COMMAND += "rm -rf ${IMAGE_ROOTFS}/usr/share/i18n/ ${IMAGE_ROOTFS}/usr/lib/ipkg/"
