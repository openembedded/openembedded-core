require xorg-lib-common.inc

DESCRIPTION = "X Test Extension: client side library"
DEPENDS += "libxext recordproto inputproto libxi"
PROVIDES = "xtst"
PR = "r1"
PE = "1"

XORG_PN = "libXtst"
