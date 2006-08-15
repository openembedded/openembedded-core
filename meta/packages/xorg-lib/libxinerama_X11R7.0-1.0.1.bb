require xorg-lib-common.inc

DESCRIPTION = "Xinerama library"
PRIORITY = "optional"

DEPENDS += " virtual/libx11 libxext xextproto xineramaproto"
PROVIDES = "xinerama"

XORG_PN = "libXinerama"

