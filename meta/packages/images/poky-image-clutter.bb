IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES}"

IMAGE_INSTALL = "\
    ${DISTRO_TASKS} \
    task-poky-clutter-core \
    task-poky-clutter-tests \
    task-poky-clutter-apps"

inherit poky-image
