require xorg-app-common.inc

SUMMARY = "XRandR: X Resize, Rotate and Reflect extension command"

DESCRIPTION = "Xrandr is used to set the size, orientation and/or \
reflection of the outputs for a screen. It can also set the screen \
size."

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=fe1608bdb33cf8c62a4438f7d34679b3"
DEPENDS += "libxrandr libxrender"
PE = "1"

SRC_URI[md5sum] = "52c3de0297bf45be6a189dc2e0515638"
SRC_URI[sha256sum] = "67b554ab975652778bef587f86dab7fec8cb95dfd21c11d98a203dac5c241e50"
