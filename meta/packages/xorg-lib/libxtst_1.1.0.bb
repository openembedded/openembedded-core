require xorg-lib-common.inc

DESCRIPTION = "X Test Extension: client side library"
DEPENDS += "libxext recordproto inputproto libxi"
PROVIDES = "xtst"
PR = "r2"
PE = "1"

XORG_PN = "libXtst"
