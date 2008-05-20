#
# Copyright (C) 2007 OpenedHand Ltd.
#
IMAGE_INSTALL = "task-poky-boot package-management"

IMAGE_LINGUAS = " "

inherit poky-image

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files"
