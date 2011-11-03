IMAGE_INSTALL = "task-core-boot task-core-apps-console task-core-ssh-openssh task-self-hosted"

POKY_EXTRA_INSTALL = "\
    task-self-hosted \
    "

IMAGE_ROOTFS_EXTRA_SPACE = "1048576"

inherit core-image

PR = "r0"
