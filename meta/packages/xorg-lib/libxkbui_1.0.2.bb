require xorg-lib-common.inc

DESCRIPTION = "X11 lbxkbui library"
LICENSE= "GPL"
PRIORITY = "optional"
PE = "1"

DEPENDS += " virtual/libx11 libxt libxkbfile"
PROVIDES = "xkbui"
