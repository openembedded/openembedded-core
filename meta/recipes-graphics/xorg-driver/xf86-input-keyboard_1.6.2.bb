require xorg-driver-input.inc

SUMMARY = "X.Org X server -- keyboard input driver"

DESCRIPTION = "keyboard is an Xorg input driver for keyboards. The \
driver supports the standard OS-provided keyboard interface.  The driver \
functions as a keyboard input device, and may be used as the X server's \
core keyboard."

LIC_FILES_CHKSUM = "file://COPYING;md5=ea2099d24ac9e316a6d4b9f20b3d4e10"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "ec2ea4c3faf5af15f2b3192d84252703"
SRC_URI[sha256sum] = "76651a84f5031f7c6ecf075d55989c04a00689642579df6d1a1bee6d5c2e5f8a"
