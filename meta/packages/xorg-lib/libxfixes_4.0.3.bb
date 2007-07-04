require xorg-lib-common.inc

DESCRIPTION = "X Fixes extension library."
LICENSE= "BSD-X"
DEPENDS += "virtual/libx11 xproto fixesproto xextproto"
PE = "1"

XORG_PN = "libXfixes"
