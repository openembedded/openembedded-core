DESCRIPTION = "A foundational basic image without support for X that can be \
reasonably used for customization and is suitable for implementations that \
conform to Linux Standard Base (LSB)."

IMAGE_FEATURES += "splash ssh-server-openssh hwcodecs package-management"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-basic \
    packagegroup-core-lsb \
    "

inherit core-image
