DESCRIPTION = "A foundational basic image without support for X that can be \
reasonably used for customization."

IMAGE_FEATURES += "splash ssh-server-openssh"

IMAGE_INSTALL = "\
    packagegroup-core-boot \
    packagegroup-core-basic \
    "

#    ${CORE_IMAGE_BASE_INSTALL} 

inherit core-image
