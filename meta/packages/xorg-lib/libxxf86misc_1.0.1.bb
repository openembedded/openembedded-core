require xorg-lib-common.inc

DESCRIPTION = "X11 XFree86 miscellaneous extension library"
DEPENDS += "libxext xf86miscproto"
PROVIDES = "xxf86misc"
PR = "r1"
PE = "1"

XORG_PN = "libXxf86misc"
