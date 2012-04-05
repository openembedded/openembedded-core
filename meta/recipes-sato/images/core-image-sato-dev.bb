#
# Copyright (C) 2007 OpenedHand Ltd.
#
DESCRIPTION = "Image with Sato for development work. It includes everything \
within core-image-sato plus a native toolchain, application development and \
testing libraries, profiling and debug symbols."

IMAGE_FEATURES += "apps-console-core ${SATO_IMAGE_FEATURES} dev-pkgs"

LICENSE = "MIT"

inherit core-image
