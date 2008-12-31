require xorg-lib-common.inc

DESCRIPTION = "X11 XFree86 video mode extension library"
DEPENDS += "libxext xf86vidmodeproto"
PR = "r1"
PE = "1"

XORG_PN = "libXxf86vm"
