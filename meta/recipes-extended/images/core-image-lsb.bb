DESCRIPTION = "A core-image-basic image suitable for implementations that \
conform to Linux Standard Base (LSB)."

IMAGE_FEATURES += "apps-console-core ssh-server-openssh"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit core-image
