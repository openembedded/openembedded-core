require xorg-lib-common.inc
PE = "1"

DESCRIPTION = "X Resize and Rotate extension library."
LICENSE = "BSD-X"

DEPENDS += " virtual/libx11 randrproto libxext xextproto libxrender renderproto"

XORG_PN = "libXrandr"

