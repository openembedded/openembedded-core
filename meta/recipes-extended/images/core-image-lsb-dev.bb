IMAGE_FEATURES += "apps-console-core dev-pkgs ssh-server-openssh"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit poky-image
