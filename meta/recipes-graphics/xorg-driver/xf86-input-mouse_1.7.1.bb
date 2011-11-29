require xorg-driver-input.inc

SUMMARY = "X.Org X server -- mouse input driver"

DESCRIPTION = "mouse is an Xorg input driver for mice. The driver \
supports most available mouse types and interfaces.  The mouse driver \
functions as a pointer input device, and may be used as the X server's \
core pointer. Multiple mice are supported by multiple instances of this \
driver."

LIC_FILES_CHKSUM = "file://COPYING;md5=237eb1d1a602d29ef2af62d8fba60f19"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "13276d780b8ef3a82088f009185bf42b"
SRC_URI[sha256sum] = "d2c5b4b9bf03f8f7ef7b37bab25197d3f99a4d889c61bb67a68df33ec2c2ff12"
