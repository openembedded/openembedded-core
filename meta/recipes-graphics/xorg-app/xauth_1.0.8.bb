require xorg-app-common.inc
SUMMARY = "X authority utilities"
DESCRIPTION = "X application to edit and display the authorization \
information used in connecting to the X server."

LIC_FILES_CHKSUM = "file://COPYING;md5=5ec74dd7ea4d10c4715a7c44f159a40b"

DEPENDS += "libxau libxext libxmu"
PE = "1"

SRC_URI[md5sum] = "50ee2ec0836c0186b05ec8fdcfd566d0"
SRC_URI[sha256sum] = "a8696ae7a50c699d5fb3a41408b60d98843d19ea46e9f09e391cb98c8f7fd4f7"
