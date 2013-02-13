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

SRC_URI[md5sum] = "3568930b0bda522e00272b64c0ca2ca2"
SRC_URI[sha256sum] = "b40c9dbd5f743ff1eb2ac81a23b9676df72e76ed3fa6408de3f8a3a260248604"

DEPENDS += "libxi mtdev"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
