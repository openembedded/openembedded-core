#
# SPDX-License-Identifier: MIT
#
# Copyright (C) 2024 Marcus Folkesson
# Author: Marcus Folkesson <marcus.folkesson@gmail.com>
#
# Writes IMAGE_BOOT_FILES to the IMAGE_BOOT_FILES_DIR directory.
#
# Usage: add "inherit image-bootfiles" to your image.
#

IMAGE_BOOT_FILES_DIR ?= "boot"

python bootfiles_populate() {
    import shutil
    from oe.bootfiles import get_boot_files

    deploy_image_dir = d.getVar("DEPLOY_DIR_IMAGE")
    boot_dir = os.path.join(d.getVar("IMAGE_ROOTFS"), d.getVar("IMAGE_BOOT_FILES_DIR"))

    boot_files = d.getVar("IMAGE_BOOT_FILES")
    if boot_files is None:
        return

    install_files = get_boot_files(deploy_image_dir, boot_files)
    if install_files is None:
        bb.warn("Could not find any boot files to install even though IMAGE_BOOT_FILES is not empty")
        return

    os.makedirs(boot_dir, exist_ok=True)
    for src, dst  in install_files:
        image_src = os.path.join(deploy_image_dir, src)
        image_dst = os.path.join(boot_dir, dst)
        os.makedirs(os.path.dirname(image_dst), exist_ok=True)
        shutil.copyfile(image_src, image_dst)
}

IMAGE_PREPROCESS_COMMAND += "bootfiles_populate;"
