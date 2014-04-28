require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=55aacd3535a741824955c5eb8f061398"

SRC_URI[md5sum] = "deaa740072c19fef8e2fb1d7787392b7"
SRC_URI[sha256sum] = "56a2d2df7bd39e29f56102c62f153e023f3e9b2f5e255309d33fab8e81945af7"

DEPENDS += "libxi mtdev libxtst"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
