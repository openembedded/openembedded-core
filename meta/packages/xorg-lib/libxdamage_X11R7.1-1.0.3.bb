require xorg-lib-common.inc

DESCRIPTION = "X Damage extension library."
LICENSE= "BSD-X"

DEPENDS += " virtual/libx11 damageproto libxfixes fixesproto xextproto"
PROVIDES = "xdamage"

XORG_PN = "libXdamage"

