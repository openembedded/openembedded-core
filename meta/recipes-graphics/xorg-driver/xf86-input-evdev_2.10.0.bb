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

DEPENDS += "mtdev libevdev"

SRC_URI[md5sum] = "b1183c55125981d346102d1be704760b"
SRC_URI[sha256sum] = "d097298eb07b7a9edf4493b5c3c058041458ca52c8c62dbd4f40b84c5086d117"
