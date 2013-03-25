require xorg-driver-input.inc

SUMMARY = "X.Org X server -- event devices (evdev) input driver"

DESCRIPTION = "evdev is an Xorg input driver for Linux's generic event \
devices. It therefore supports all input devices that the kernel knows \
about, including most mice and keyboards. \
\
The evdev driver can serve as both a pointer and a keyboard input \
device, and may be used as both the core keyboard and the core pointer. \
Multiple input devices are supported by multiple instances of this \
driver, with one Load directive for evdev in the Module section of your \
xorg.conf for each input device that will use this driver. "

LIC_FILES_CHKSUM = "file://COPYING;md5=fefe33b1cf0cacba0e72e3b0fa0f0e16"

PR = "${INC_PR}.1"

DEPENDS += "mtdev"

SRC_URI[md5sum] = "f68920ce2921a88b4662acc972bf3a4a"
SRC_URI[sha256sum] = "eb389413602c3d28c44bbfab0477c98582f0e2f5be5f41986e58e93a033fa504"
