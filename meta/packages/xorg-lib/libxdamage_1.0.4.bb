require xorg-lib-common.inc

DESCRIPTION = "X11 damaged region extension library"
LICENSE= "BSD-X"
DEPENDS += "damageproto libxfixes"
PROVIDES = "xdamage"
PR = "r1"
PE = "1"

XORG_PN = "libXdamage"
