IMAGE_FEATURES += "apps-console-core ssh-server-openssh"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit core-image
