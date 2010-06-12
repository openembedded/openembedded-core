require xorg-lib-common.inc

DESCRIPTION = "X11 Direct Graphics Access extension library"
DEPENDS += "libxext xf86dgaproto"
PR = "r1"
PE = "1"

XORG_PN = "libXxf86dga"

LIC_FILES_CHKSUM = "file://COPYING;md5=978fbe536ab97ed49f45ba81159fe4b3"
