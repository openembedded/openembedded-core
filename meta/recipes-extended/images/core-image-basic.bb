IMAGE_FEATURES += "apps-console-core ssh-server-openssh"

IMAGE_INSTALL = "\
    task-core-boot \
    task-core-basic \
    "

#    ${POKY_BASE_INSTALL} 

inherit core-image
