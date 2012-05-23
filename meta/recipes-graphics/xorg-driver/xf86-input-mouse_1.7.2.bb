require xorg-driver-input.inc

SUMMARY = "X.Org X server -- mouse input driver"

DESCRIPTION = "mouse is an Xorg input driver for mice. The driver \
supports most available mouse types and interfaces.  The mouse driver \
functions as a pointer input device, and may be used as the X server's \
core pointer. Multiple mice are supported by multiple instances of this \
driver."

LIC_FILES_CHKSUM = "file://COPYING;md5=237eb1d1a602d29ef2af62d8fba60f19"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "871c828b88e9c973f1457724b35576fb"
SRC_URI[sha256sum] = "332b7357c18e6b9daba51c8ed48ce118e9b51fb5990b6a2a68637be62da7413b"
