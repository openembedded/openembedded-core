DESCRIPTION = "Basic image without X support suitable for Linux Standard Base \
(LSB) implementations. It includes the full meta-toolchain, plus development \
headers and libraries to form a standalone SDK."

IMAGE_FEATURES += "splash tools-sdk dev-pkgs ssh-server-openssh \
	tools-debug tools-profile tools-testapps debug-tweaks"


IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    packagegroup-core-basic \
    packagegroup-core-lsb \
    kernel-dev \
    "

inherit core-image
