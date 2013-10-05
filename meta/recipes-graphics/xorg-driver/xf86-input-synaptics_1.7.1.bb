require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=55aacd3535a741824955c5eb8f061398"

SRC_URI[md5sum] = "6505de717972b6a24b8eb13e69eb996c"
SRC_URI[sha256sum] = "db5825660e1fb23190697f609bf75d4450fe707344a14298e1c9b47039bbb58e"

DEPENDS += "libxi mtdev libxtst"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
