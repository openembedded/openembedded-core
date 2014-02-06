require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=55aacd3535a741824955c5eb8f061398"

SRC_URI[md5sum] = "74c83e6cb53a0e15bcbe7cc73d63d2a1"
SRC_URI[sha256sum] = "8b2a972043961195d056b84346317ec42bfa029095c9ee7aaf6deceba12e32d5"

DEPENDS += "libxi mtdev libxtst"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
