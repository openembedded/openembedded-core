IMAGE_INSTALL = "task-core-boot task-core-apps-console task-core-ssh-openssh task-self-hosted"

CORE_IMAGE_EXTRA_INSTALL = "\
    task-self-hosted \
    "

IMAGE_FEATURES += "x11-mini package-management"

inherit core-image

PR = "r2"
