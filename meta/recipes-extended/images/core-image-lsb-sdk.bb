DESCRIPTION = "A core-image-lsb that includes everything in meta-toolchain \
but also includes development headers and libraries to form a complete \
standalone SDK."

IMAGE_FEATURES += "apps-console-core tools-sdk dev-pkgs ssh-server-openssh"
EXTRA_IMAGE_FEATURES = "tools-debug tools-profile tools-testapps debug-tweaks"


IMAGE_INSTALL = "\
    ${CORE_IMAGE_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit core-image
