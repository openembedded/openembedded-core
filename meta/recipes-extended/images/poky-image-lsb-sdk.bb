IMAGE_FEATURES += "apps-console-core tools-debug tools-profile tools-sdk dev-pkgs"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-poky-basic \
    task-poky-lsb \
    "

inherit poky-image
