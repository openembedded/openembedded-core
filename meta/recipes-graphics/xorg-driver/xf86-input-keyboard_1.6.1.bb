require xorg-driver-input.inc

SUMMARY = "X.Org X server -- keyboard input driver"

DESCRIPTION = "keyboard is an Xorg input driver for keyboards. The \
driver supports the standard OS-provided keyboard interface.  The driver \
functions as a keyboard input device, and may be used as the X server's \
core keyboard."

LIC_FILES_CHKSUM = "file://COPYING;md5=ea2099d24ac9e316a6d4b9f20b3d4e10"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "09744e8dc9a1fe5e61927c1073cd3428"
SRC_URI[sha256sum] = "aa9ec96e7f7f87bc086cb86b871ee6f4b9a7809fb1e7d50d0abbd7c2e50a8cc3"
