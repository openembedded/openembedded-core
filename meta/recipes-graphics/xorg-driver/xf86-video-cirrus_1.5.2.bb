require xorg-driver-video.inc

SUMMARY = "X.Org X server -- cirrus display driver"
DESCRIPTION = "cirrus is an Xorg driver for Cirrus Logic VGA adapters. These \
devices are not so common in the wild anymore, but QEMU can emulate one, so \
the driver is still useful."

LIC_FILES_CHKSUM = "file://COPYING;md5=6ddc7ca860dc5fd014e7f160ea699295"

SRC_URI[md5sum] = "91fd6b677d62027cd3001debb587a6a6"
SRC_URI[sha256sum] = "3361e1a65d9b84c464752fd612bdf6087622c6dd204121715366a170e5c3ccd7"

DEPENDS += "libpciaccess"
