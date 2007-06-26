#
# Copyright (C) 2007 OpenedHand Ltd.
#
PR = "r2"

IMAGE_INSTALL = "task-oh-boot"

IMAGE_LINGUAS = " "

require poky-image.inc

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files"
