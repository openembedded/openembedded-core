IMAGE_FEATURES += "apps-console-core "

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-poky-basic \
    "

inherit poky-image
