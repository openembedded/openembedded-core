require xorg-lib-common.inc

DESCRIPTION = "X Test Extension: client side library"
PRIORITY = "optional"

DEPENDS += " virtual/libx11 libxext recordproto xextproto inputproto"
PROVIDES = "xtst"

XORG_PN = "libXtst"

