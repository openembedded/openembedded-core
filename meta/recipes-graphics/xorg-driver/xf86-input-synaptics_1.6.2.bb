require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=e395e21f3c21d4fc3a243783e85e9ab5"

PR = "${INC_PR}.1"

SRC_URI[md5sum] = "9914022a173e3f0ccfe7137ab84f6133"
SRC_URI[sha256sum] = "c3f7d6a085d480c352f030aeb43db2e5560d1468ed34be24d44a0fc3fda25920"

DEPENDS += "libxi mtdev"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
