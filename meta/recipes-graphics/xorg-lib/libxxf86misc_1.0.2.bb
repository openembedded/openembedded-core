DESCRIPTION = "X11 XFree86 miscellaneous extension library"

require xorg-lib-common.inc

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING;md5=27c91ecc435bd3d2bfad868914c94b45"

DEPENDS += "libxext xf86miscproto"
PROVIDES = "xxf86misc"

PR = "r0"
PE = "1"

XORG_PN = "libXxf86misc"

SRC_URI[md5sum] = "51fed53e8de067d4b8666f75a0212400"
SRC_URI[sha256sum] = "8e64b6b1bf34fcd112b1fd866d77e110b47794e825a996e4492c0ee408957cb3"
