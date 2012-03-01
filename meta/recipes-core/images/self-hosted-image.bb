IMAGE_INSTALL = "task-core-boot task-core-apps-console task-core-ssh-openssh task-self-hosted"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/LICENSE;md5=3f40d7994397109285ec7b81fdeb3b58 \
                    file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

PR = "r5"

CORE_IMAGE_EXTRA_INSTALL = "\
    task-self-hosted \
    "

IMAGE_FEATURES += "x11-mini package-management"

# Ensure there's enough space to do a core-image-minimal build, with rm_work enabled
IMAGE_ROOTFS_EXTRA_SPACE = "2621440"

# Do a quiet boot with limited console messages
APPEND += "quiet"

IMAGE_FSTYPES = "vmdk"

inherit core-image
