#
# Copyright (C) 2010 Intel Corporation.
#

DESCRIPTION = "Minimal Real-Time Linux Image"
DEPENDS = "linux-yocto-rt"

require recipes-core/images/poky-image-minimal.bb

IMAGE_INSTALL += "rt-tests"

LICENSE = "MIT"
