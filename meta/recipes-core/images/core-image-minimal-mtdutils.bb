#
# Copyright (C) 2008 OpenedHand Ltd.
#
require core-image-minimal.bb

DESCRIPTION = "A core-image-minimal image that has support the Minimal MTD \
Utilities, which let the user interact with the MTD subsystem in the kernel \
to perform operations on flash devices."

IMAGE_INSTALL += "mtd-utils"
