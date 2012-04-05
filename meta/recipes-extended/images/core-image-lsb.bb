DESCRIPTION = "A foundational basic image without support for X that can be \
reasonably used for customization and is suitable for implementations that \
conform to Linux Standard Base (LSB)."

IMAGE_FEATURES += "apps-console-core ssh-server-openssh"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit core-image
