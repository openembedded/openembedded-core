require xorg-lib-common.inc

DESCRIPTION = "X11 miscellaneous extension library"
DEPENDS += "xproto virtual/libx11 xextproto libxau libxdmcp"
PROVIDES = "xext"
PE = "1"

XORG_PN = "libXext"

BBCLASSEXTEND = "nativesdk"
