#
# Copyright (C) 2010 Intel Corporation.
#

DESCRIPTION = "Real-Time Linux Image"
DEPENDS = "linux-yocto-rt"

require recipes-core/images/core-image-minimal.bb

IMAGE_INSTALL += "rt-tests"

LICENSE = "MIT"
