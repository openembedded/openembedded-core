require xorg-lib-common.inc
PE = "1"

DESCRIPTION = "X Fixes extension library."
LICENSE= "BSD-X"

DEPENDS += " virtual/libx11 xproto fixesproto xextproto"

XORG_PN = "libXfixes"

