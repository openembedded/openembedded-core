require xorg-lib-common.inc

DESCRIPTION = "X11 Xinerama extension library"
DEPENDS += "libxext xineramaproto"
PROVIDES = "xinerama"
PR = "r3"
PE = "1"

XORG_PN = "libXinerama"
