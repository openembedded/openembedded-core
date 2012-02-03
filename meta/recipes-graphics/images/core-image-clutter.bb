DESCRIPTION = "An image with support for the Open GL-based toolkit Clutter, \
which enables development of rich and animated graphical user interfaces."

IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES}"

LICENSE = "MIT"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    task-core-clutter-core \
    task-core-clutter-tests \
    task-core-clutter-apps"

inherit core-image
