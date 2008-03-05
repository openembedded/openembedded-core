require xorg-lib-common.inc

DESCRIPTION = "X11 miscellaneous extension library"
DEPENDS += "xproto virtual/libx11 xextproto libxau"
PROVIDES = "xext"
PR = "r1"
PE = "1"

XORG_PN = "libXext"
