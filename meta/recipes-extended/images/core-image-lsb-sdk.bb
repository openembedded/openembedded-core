IMAGE_FEATURES += "apps-console-core tools-sdk dev-pkgs ssh-server-openssh"
EXTRA_IMAGE_FEATURES = "tools-debug tools-profile tools-testapps debug-tweaks"


IMAGE_INSTALL = "\
    ${POKY_BASE_INSTALL} \
    task-core-basic \
    task-core-lsb \
    "

inherit core-image
