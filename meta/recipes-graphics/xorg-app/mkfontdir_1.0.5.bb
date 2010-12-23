require xorg-app-common.inc

DESCRIPTION = "a program to create an index of X font files in a directory"

PE = "1"
PR = "r1"

RDEPENDS_${PN} += "mkfontscale"

BBCLASSEXTEND = "native"

LIC_FILES_CHKSUM = "file://COPYING;md5=b4fcf2b90cadbfc15009b9e124dc3a3f"

SRC_URI[md5sum] = "9365ac66d19186eaf030482d312fca06"
SRC_URI[sha256sum] = "a534650cff503619f9101577d816cde283da993bc039273477bd8dfbd01a2d0b"
