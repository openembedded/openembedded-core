require xorg-lib-common.inc

DESCRIPTION = "X11 keyboard UI presentation library"
LICENSE= "GPL"
DEPENDS += "virtual/libx11 libxt libxkbfile"
PROVIDES = "xkbui"
PR = "r1"
PE = "1"
