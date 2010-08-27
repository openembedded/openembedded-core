require xorg-app-common.inc

DESCRIPTION = "a program to create an index of X font files in a directory"

PE = "1"

RDEPENDS += "mkfontscale"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=b4fcf2b90cadbfc15009b9e124dc3a3f"
