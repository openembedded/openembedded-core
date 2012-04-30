require xorg-app-common.inc
SUMMARY = "X authority utilities"
DESCRIPTION = "X application to edit and display the authorization \
information used in connecting to the X server."

LIC_FILES_CHKSUM = "file://COPYING;md5=5ec74dd7ea4d10c4715a7c44f159a40b"

DEPENDS += "libxau libxext libxmu"
PR = "${INC_PR}.0"
PE = "1"

SRC_URI[md5sum] = "cbcbd8f2156a53b609800bec4c6b6c0e"
SRC_URI[sha256sum] = "84f78c08ebc6687e2e36c9ff1f5610988c6a03fc5bf51ef89aec4d155de3028d"
