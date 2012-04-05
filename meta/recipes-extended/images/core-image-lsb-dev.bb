DESCRIPTION = "Basic image without X support suitable for development work. It \
can be used for customization and implementations that conform to Linux \
Standard Base (LSB)."

IMAGE_FEATURES += "apps-console-core dev-pkgs ssh-server-openssh"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit core-image
