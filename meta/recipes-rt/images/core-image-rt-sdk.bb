#
# Copyright (C) 2011 Intel Corporation.
#

DESCRIPTION = "Real-Time Linux Image with SDK support"
DEPENDS = "linux-yocto-rt"

require recipes-core/images/core-image-minimal.bb

IMAGE_FEATURES += "dev-pkgs tools-sdk"
EXTRA_IMAGE_FEATURES += "tools-debug tools-profile tools-testapps debug-tweaks"

IMAGE_INSTALL += "rt-tests"

LICENSE = "MIT"
