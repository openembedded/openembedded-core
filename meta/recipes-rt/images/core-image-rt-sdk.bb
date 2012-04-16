#
# Copyright (C) 2011 Intel Corporation.
#
require recipes-core/images/core-image-minimal.bb

DESCRIPTION = "Small image capable of booting a device with a test suite and \
tools for real-time use. It includes the full meta-toolchain, development \
headers and libraries to form a standalone SDK."
DEPENDS = "linux-yocto-rt"

IMAGE_FEATURES += "dev-pkgs tools-sdk"
EXTRA_IMAGE_FEATURES += "tools-debug tools-profile tools-testapps debug-tweaks"

IMAGE_INSTALL += "rt-tests"

LICENSE = "MIT"
