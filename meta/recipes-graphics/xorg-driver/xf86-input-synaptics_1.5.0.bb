require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=e395e21f3c21d4fc3a243783e85e9ab5"

PR = "${INC_PR}.2"

SRC_URI[md5sum] = "41ee749ecbfef98f7fba708cb2afae87"
SRC_URI[sha256sum] = "95cc5399fc49c9a35b02c2272cd99b8438f4609b219278c66a79e74c916a1c4e"

DEPENDS += "libxi"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
