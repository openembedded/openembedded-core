require xorg-lib-common.inc

DESCRIPTION = "X Screen Saver extension library"
LICENSE = "GPL"
DEPENDS += "libxext scrnsaverproto"
PROVIDES = "libxss"
RREPLACES = "libxss"
PR = "r0"
PE = "1"

XORG_PN = "libXScrnSaver"

SRC_URI[md5sum] = "898794bf6812fc9be9bf1bb7aa4d2b08"
SRC_URI[sha256sum] = "ce3a66e2f6fa85b22280ab9cc1b2a113a2cb0ade9470914c7c3d6ac1d44b259a"
