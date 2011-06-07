#
# Copyright (C) 2007 OpenedHand Ltd.
#


IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES} dev-pkgs tools-sdk qt4-pkgs"
EXTRA_IMAGE_FEATURES += "tools-debug tools-profile tools-testapps debug-tweaks"

LICENSE = "MIT"

inherit core-image
