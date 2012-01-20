#
# Copyright (C) 2007 OpenedHand Ltd.
#
DESCRIPTION = "A core-image-sato image that includes everything in \
meta-toolchain. The image also includes development headers and libraries \
to form a complete standalone SDK."

IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES} dev-pkgs tools-sdk qt4-pkgs"
EXTRA_IMAGE_FEATURES += "tools-debug tools-profile tools-testapps debug-tweaks"

LICENSE = "MIT"

inherit core-image
