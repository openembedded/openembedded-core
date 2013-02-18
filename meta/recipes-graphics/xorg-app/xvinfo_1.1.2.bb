require xorg-app-common.inc

SUMMARY = "Print out X-Video extension adaptor information"

DESCRIPTION = "xvinfo prints out the capabilities of any video adaptors \
associated with the display that are accessible through the X-Video \
extension."

LIC_FILES_CHKSUM = "file://COPYING;md5=b664101ad7a1dc758a4c4109bf978e68"
DEPENDS += " libxv"
PE = "1"
PR = "${INC_PR}.0"

SRC_URI[md5sum] = "1fbd65e81323a8c0a4b5e24db0058405"
SRC_URI[sha256sum] = "eed3d90ffd788ef728c4a5e7aa4bd86dc6bbcebac929caf7a0479cf8b53b50e3"
