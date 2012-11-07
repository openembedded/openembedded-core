require xorg-driver-input.inc

SUMMARY = "X.Org X server -- mouse input driver"

DESCRIPTION = "mouse is an Xorg input driver for mice. The driver \
supports most available mouse types and interfaces.  The mouse driver \
functions as a pointer input device, and may be used as the X server's \
core pointer. Multiple mice are supported by multiple instances of this \
driver."

LIC_FILES_CHKSUM = "file://COPYING;md5=90ea9f90d72b6d9327dede5ffdb2a510"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "f314c56e4ac6c8fc0cc2710892d5776e"
SRC_URI[sha256sum] = "f5b97aac9aab8fa8b933e960631441ae23b18681c8bf3d5007c00da838f9c9c8"

