DESCRIPTION = "X11 XFree86 miscellaneous extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=27c91ecc435bd3d2bfad868914c94b45"

DEPENDS += "libxext xf86miscproto"
PROVIDES = "xxf86misc"

PR = "r0"
PE = "1"

XORG_PN = "libXxf86misc"
