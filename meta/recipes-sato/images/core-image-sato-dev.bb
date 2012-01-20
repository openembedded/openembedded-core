#
# Copyright (C) 2007 OpenedHand Ltd.
#
DESCRIPTION = "A core-image-sato image suitable for development that also \
includes a native toolchain and libraries needed to build applications on the \
device itself."

IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES} dev-pkgs"

LICENSE = "MIT"

inherit core-image
