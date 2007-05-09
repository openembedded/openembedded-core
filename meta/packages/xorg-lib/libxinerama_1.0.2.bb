require xorg-lib-common.inc

DESCRIPTION = "Xinerama library"
PRIORITY = "optional"
PE = "1"

DEPENDS += " virtual/libx11 libxext xextproto xineramaproto"
PROVIDES = "xinerama"

XORG_PN = "libXinerama"

