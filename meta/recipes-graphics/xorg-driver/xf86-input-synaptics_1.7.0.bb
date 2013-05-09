require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=55aacd3535a741824955c5eb8f061398"

SRC_URI[md5sum] = "5e4f232a18a1741e4c34895d28238f8d"
SRC_URI[sha256sum] = "d6f1ad0b0653dddbe2e2db7e2f06c1860e491045e87a6577b63568d65e5d0f0e"

DEPENDS += "libxi mtdev"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
