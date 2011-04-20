IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES}"

LICENSE = "MIT"

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-core-clutter-core \
    task-core-clutter-tests \
    task-core-clutter-apps"

inherit core-image
