#
# Copyright (C) 2007 OpenedHand Ltd.
#
IMAGE_INSTALL = "task-core-boot ${ROOTFS_PKGMANAGE_BOOTSTRAP}"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit poky-image

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files ; "
