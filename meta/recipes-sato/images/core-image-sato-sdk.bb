DESCRIPTION = "Image with Sato support that includes everything within \
core-image-sato plus meta-toolchain, development headers and libraries to \
form a standalone SDK."

IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES} dev-pkgs tools-sdk qt4-pkgs \
	tools-debug tools-profile tools-testapps debug-tweaks"

SSHSERVER_IMAGE_FEATURES = "ssh-server-openssh"

LICENSE = "MIT"

inherit core-image

IMAGE_INSTALL += "kernel-dev"

