require xorg-app-common.inc

SUMMARY = "Window information utility for X"

DESCRIPTION = "Xwininfo is a utility for displaying information about \
windows.  Information may include window position, size, color depth, \
and a number of other items."

LIC_FILES_CHKSUM = "file://COPYING;md5=78976cd3115f6faf615accc4e094d90e"
DEPENDS += "libxext libxmu"

PR = "r0"
PE = "0"

SRC_URI[md5sum] = "7a7f44b826d877ba39c19cf5913978d7"
SRC_URI[sha256sum] = "645a57be4870fb6aba459e7dc96f4d8eaafa3b31bbcd42c997ac4cb021a2343d"
