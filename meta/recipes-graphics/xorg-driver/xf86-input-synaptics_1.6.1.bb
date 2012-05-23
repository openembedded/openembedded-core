require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=e395e21f3c21d4fc3a243783e85e9ab5"

PR = "${INC_PR}.0"

SRC_URI[md5sum] = "d10a7ee362d015975fbef11c6beaac97"
SRC_URI[sha256sum] = "1ab1459ea340f371c40be7d6a780e43bdaa2d9799c1de21145e3b5808d0eab3c"

DEPENDS += "libxi"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
