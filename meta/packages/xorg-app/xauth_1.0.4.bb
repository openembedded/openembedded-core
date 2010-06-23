require xorg-app-common.inc

DESCRIPTION = "X application to edit and display the authorization \
information used in connecting to the X server"

LIC_FILES_CHKSUM = "file://COPYING;md5=5ec74dd7ea4d10c4715a7c44f159a40b"

DEPENDS += "libxau libxext libxmu"
PR = "r0"
PE = "1"
