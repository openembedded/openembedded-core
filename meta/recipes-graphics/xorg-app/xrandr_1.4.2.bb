require xorg-app-common.inc

SUMMARY = "XRandR: X Resize, Rotate and Reflect extension command"

DESCRIPTION = "Xrandr is used to set the size, orientation and/or \
reflection of the outputs for a screen. It can also set the screen \
size."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=fe1608bdb33cf8c62a4438f7d34679b3"
DEPENDS += "libxrandr libxrender"
PE = "1"

SRC_URI[md5sum] = "78fd973d9b532106f8777a3449176148"
SRC_URI[sha256sum] = "b2e76ee92ff827f1c52ded7c666fe6f2704ca81cdeef882397da4e3e8ab490bc"
