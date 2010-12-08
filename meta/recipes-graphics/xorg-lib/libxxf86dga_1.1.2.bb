require xorg-lib-common.inc

DESCRIPTION = "X11 Direct Graphics Access extension library"
DEPENDS += "libxext xf86dgaproto"
PR = "r1"
PE = "1"

XORG_PN = "libXxf86dga"

LIC_FILES_CHKSUM = "file://COPYING;md5=abb99ac125f84f424a4278153988e32f"

SRC_URI[md5sum] = "bbd5fdf63d4c107c8cb710d4df2012b4"
SRC_URI[sha256sum] = "1ba652f562ce3fb3fef092ce5485eb7ef15b521124c901977b56d6f324605a06"
