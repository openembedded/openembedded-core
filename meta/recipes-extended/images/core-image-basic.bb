DESCRIPTION = "A foundational basic image without support for X that can be \
reasonably used for customization."

IMAGE_FEATURES += "apps-console-core ssh-server-openssh"

IMAGE_INSTALL = "\
    task-core-boot \
    task-core-basic \
    "

#    ${CORE_IMAGE_BASE_INSTALL} 

inherit core-image
