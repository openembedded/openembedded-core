IMAGE_FEATURES += "apps-console-core dev-pkgs ssh-server-openssh"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-poky-basic \
    task-poky-lsb \
    "

inherit poky-image
