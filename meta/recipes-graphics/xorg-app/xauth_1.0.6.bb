require xorg-app-common.inc
SUMMARY = "X authority utilities"
DESCRIPTION = "X application to edit and display the authorization \
information used in connecting to the X server."

LIC_FILES_CHKSUM = "file://COPYING;md5=5ec74dd7ea4d10c4715a7c44f159a40b"

DEPENDS += "libxau libxext libxmu"
PR = "r0"
PE = "1"

SRC_URI[md5sum] = "105f5b00bb9293b3db36f7e500d4f950"
SRC_URI[sha256sum] = "a686406951f0ed6be45bc26182a1423e2cdffad2d492b33ff3fbf72f7bcb6b0b"
