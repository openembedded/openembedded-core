#
# Copyright (C) 2007 OpenedHand Ltd.
#
IMAGE_INSTALL = "task-core-boot ${ROOTFS_PKGMANAGE}"

IMAGE_FEATURES += "dev-pkgs"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit core-image

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files ; "
