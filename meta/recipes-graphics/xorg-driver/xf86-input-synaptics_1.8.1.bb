require xorg-driver-input.inc

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=55aacd3535a741824955c5eb8f061398"

SRC_URI[md5sum] = "ed70d0cba94262a4008dcef654ab34a9"
SRC_URI[sha256sum] = "d74fdaf29e9888a2cb494e16d0a9ddb24265c5c765b05392b69c50e84ffbf09a"

DEPENDS += "libxi mtdev libxtst libevdev"

FILES_${PN} += "${datadir}/X11/xorg.conf.d"
