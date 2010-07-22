IMAGE_FEATURES += "apps-console-core "

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-poky-small-utils \
    task-poky-basic-libs \
    task-poky-basic-utils \
    task-poky-extended-libs \
    task-poky-extended-utils \
    task-poky-network-services \
    "

inherit poky-image
