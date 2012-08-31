DESCRIPTION = "Basic image without X support suitable for development work. It \
can be used for customization and implementations that conform to Linux \
Standard Base (LSB)."

IMAGE_FEATURES += "splash dev-pkgs ssh-server-openssh"

IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-basic \
    packagegroup-core-lsb \
    "

inherit core-image
