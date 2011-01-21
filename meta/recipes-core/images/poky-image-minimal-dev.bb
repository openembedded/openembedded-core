#
# Copyright (C) 2007 OpenedHand Ltd.
#
IMAGE_INSTALL = "task-poky-boot ${ROOTFS_PKGMANAGE}"

IMAGE_FEATURES += "dev-pkgs"

IMAGE_LINGUAS = " "

LICENSE = "MIT"

inherit poky-image

# remove not needed ipkg informations
ROOTFS_POSTPROCESS_COMMAND += "remove_packaging_data_files ; "
