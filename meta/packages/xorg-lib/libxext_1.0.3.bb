require xorg-lib-common.inc
PE = "1"

DESCRIPTION = "X Server Extension library"
PRIORITY = "optional"

DEPENDS += " xproto virtual/libx11 xextproto libxau"
PROVIDES = "xext"

XORG_PN = "libXext"

