require xorg-app-common.inc
SUMMARY = "X authority utilities"
DESCRIPTION = "X application to edit and display the authorization \
information used in connecting to the X server"

LIC_FILES_CHKSUM = "file://COPYING;md5=5ec74dd7ea4d10c4715a7c44f159a40b"

DEPENDS += "libxau libxext libxmu"
PR = "r0"
PE = "1"

SRC_URI[md5sum] = "46fc44e5e947d3720f3be5054044ff0e"
SRC_URI[sha256sum] = "6d139500ff1daf806525adf071f8c1778ad138a0378c73ea831ad18847ad746c"
