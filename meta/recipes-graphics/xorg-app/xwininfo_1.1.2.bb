require xorg-app-common.inc

SUMMARY = "Window information utility for X"

DESCRIPTION = "Xwininfo is a utility for displaying information about \
windows.  Information may include window position, size, color depth, \
and a number of other items."

LIC_FILES_CHKSUM = "file://COPYING;md5=78976cd3115f6faf615accc4e094d90e"
DEPENDS += "libxext libxmu"

PR = "r0"
PE = "0"

SRC_URI[md5sum] = "9e8b58c8aa6172e87ab4f9cf3612fedd"
SRC_URI[sha256sum] = "8fa66c9ce02da257613fa428137ab9efc89c8f9939c074513dbc0f407dc9ac3a"
