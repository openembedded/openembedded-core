require xorg-driver-input.inc

#SRC_URI += "file://configurefix.patch"

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=55aacd3535a741824955c5eb8f061398"

SRC_URI += "file://always_include_xorg_server.h.patch"

SRC_URI[md5sum] = "27a3f2b31606a13dd6b58d419978d64f"
SRC_URI[sha256sum] = "9bf27632aaa6c5e62621ca9c2ca00f9b309c85b039ee33cd592b189fc872c37a"

DEPENDS += "libxi mtdev libxtst libevdev"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
