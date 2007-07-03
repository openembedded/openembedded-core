require xorg-lib-common.inc

DESCRIPTION = "X Server Extension library"
PRIORITY = "optional"

DEPENDS += " xproto virtual/libx11 xextproto libxau"
PROVIDES = "xext"
PE = "1"

XORG_PN = "libXext"

