IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES}"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-poky-clutter-core \
    task-poky-clutter-tests \
    task-poky-clutter-apps"

inherit poky-image
