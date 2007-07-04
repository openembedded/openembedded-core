require xorg-lib-common.inc

DESCRIPTION = "X Screen Saver extension library"
LICENSE = "GPL"
DEPENDS += "libxext scrnsaverproto"
PROVIDES = "libxss"
RREPLACES = "libxss"
PR = "r1"
PE = "1"

XORG_PN = "libXScrnSaver"
