#
# Copyright (C) 2008 Intel Corporation.
#

IMAGE_INSTALL = "task-moblin-boot ${ROOTFS_PKGMANAGE}"

IMAGE_LINGUAS = " "

inherit moblin-image

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files"
