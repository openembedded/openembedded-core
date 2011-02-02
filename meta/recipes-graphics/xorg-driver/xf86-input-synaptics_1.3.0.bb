require xf86-input-common.inc

SUMMARY = "X.Org X server -- synaptics touchpad input driver"

DESCRIPTION = "synaptics is an Xorg input driver for the touchpads from \
Synaptics Incorporated. Even though these touchpads (by default, \
operating in a compatibility mode emulating a standard mouse) can be \
handled by the normal evdev or mouse drivers, this driver allows more \
advanced features of the touchpad to become available."

LIC_FILES_CHKSUM = "file://COPYING;md5=e395e21f3c21d4fc3a243783e85e9ab5"

DEPENDS += "libxi"

#
# the xorg-synaptics.pc has hardcoded sdkdir=/usr/include/xorg, which is not correct
# for cross compiling, so pass the correct sdkdir as include path
#
EXTRA_OEMAKE += " sdkdir=${STAGING_INCDIR}/xorg "

SRC_URI[md5sum] = "b4e58eba1bdca13f0929a4b03b262135"
SRC_URI[sha256sum] = "30a33250c4f3d2daa8a61cab847dc7befd3248db0fca139d17fd7b890b5a8fd7"
