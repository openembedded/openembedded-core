require xorg-lib-common.inc

DESCRIPTION = "Xinerama library"
PRIORITY = "optional"
PE = "1"
PR = "r1"

DEPENDS += " virtual/libx11 libxext xextproto xineramaproto"
PROVIDES = "xinerama"

SRC_URI += "file://configure_fix.patch;patch=1"

XORG_PN = "libXinerama"

