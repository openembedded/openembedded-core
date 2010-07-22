IMAGE_FEATURES += "apps-console-core "

IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-poky-small \
    task-poky-basic-libs \
    task-poky-basic-libs-dbg \
    task-poky-basic-libs-dev \
    task-poky-basic-utils \
    task-poky-basic-utils-dbg \
    task-poky-basic-utils-dev \
    task-poky-minimal-extras \
    task-poky-minimal-extras-dbg \
    task-poky-minimal-extras-dev \
    task-poky-pkg-managment-opkg \
    task-poky-pkg-managment-opkg-dbg \
    task-poky-pkg-managment-opkg-dev \
    task-poky-network-services \
    task-poky-network-services-dbg \
    task-poky-network-services-dev \
    "

inherit poky-image
